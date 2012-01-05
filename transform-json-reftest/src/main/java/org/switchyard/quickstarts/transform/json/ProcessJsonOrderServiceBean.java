package org.switchyard.quickstarts.transform.json;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(ProcessJsonOrderService.class)
public class ProcessJsonOrderServiceBean implements ProcessJsonOrderService {
    @Inject @Reference("OrderService")
    JsonOrderService _orderService;
    
    @Override
    public String process(String orderJson) {
        return _orderService.submitOrder(orderJson);
    }

}
