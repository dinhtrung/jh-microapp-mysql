package com.ft.service.app;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.ft.config.ApplicationProperties;
import com.ft.repository.HttpProfileRepository;
import com.ft.repository.SmppProfileRepository;
import com.ft.repository.SmppRoutingInfoRepository;
import com.ft.service.dto.MessageDTO;
import com.hazelcast.core.HazelcastInstance;

@Service
@Scope("singleton")
public class SmppProcessService {

    private static final Logger log = LoggerFactory.getLogger(SmppProcessService.class);

    @Autowired
    private ApplicationProperties props;

    @Autowired
    private HttpProfileRepository httpProfileRepo;
    
    @Autowired
    private SmppProfileRepository smppProfileRepo;
    
    @Autowired
    private SmppRoutingInfoRepository routingInfoRepo;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * Handling delivery report
     *
     * @param dlr
     */
    public void processDLR(DeliveryReceipt dlr) {
        log.debug("Receive Delivery Receipt: " + dlr.getMessageId());
        // TODO: Send a request to Delivery Notification endpoint
        MessageDTO message = (MessageDTO) hazelcastInstance.getMap("messages").get(dlr.getMessageId());
        if (message != null) {
        	try {
            	ResponseEntity<String> response = restTemplate.postForEntity("https://httpbin.org/status", message, String.class);
            	log.info("<< RESPONSE: ", response);
            } catch (HttpStatusCodeException e) {
    			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
    			}
    		} catch (Exception e1) {
    			log.error("Cannot send message", e1);
    		}
        }
        
    }

    /**
     * Convert MO SMS to a web trigger
     *
     * @param message
     */
    public void processMO(BaseSm message) {
        String msisdn = message.getSourceAddress().getAddress();
        String shortcode = message.getDestAddress().getAddress();
        String shortMsg = "";
        try {
            Tlv messagePayload = message.getOptionalParameter(SmppConstants.TAG_MESSAGE_PAYLOAD);
            if (messagePayload != null) {
                shortMsg = messagePayload.getValueAsString();
            } else if (message.getDataCoding() == SmppConstants.DATA_CODING_UCS2) {
            	shortMsg = CharsetUtil.decode(message.getShortMessage(), CharsetUtil.CHARSET_UCS_2);
            } else {
                shortMsg = CharsetUtil.decode(message.getShortMessage(), CharsetUtil.CHARSET_GSM);
            }
        } catch (Exception e) {
            log.error("Cannot parse message", e);
        }
        log.info("Mobile Originated SMS: " + msisdn + " --> " + shortcode + " : [" + shortMsg + "]");
        
        // TODO: Send a request to Delivery Notification endpoint
        MessageDTO req = new MessageDTO();
        req.setRequestAt(ZonedDateTime.now());
        req.setDeliveredAt(ZonedDateTime.now());
        req.setDestination(shortcode);
        req.setSource(msisdn);
        req.setText(shortMsg);
        try {
        	ResponseEntity<String> response = restTemplate.postForEntity("https://httpbin.org/status", req, String.class);
        	log.info("<< RESPONSE: ", response);
        } catch (HttpStatusCodeException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			}
		} catch (Exception e1) {
			log.error("Cannot send message", e1);
		}
    }
}
