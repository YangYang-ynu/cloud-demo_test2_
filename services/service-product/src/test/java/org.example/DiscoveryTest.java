package org.example;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
public class DiscoveryTest {
    //spring标准获取示例
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;
//    @Test
//    void nacosServiceDiscoveryTest() throws NacosException {
//        for (String serviceInstance : nacosDiscoveryProperties.getService()) {
//            System.out.println(serviceInstance);
//            List<ServiceInstance> instances = discoveryClient.getInstances(serviceInstance);
//            for (ServiceInstance instance : instances) {
//                System.out.println("ip "+instance.getHost()+"port "+instance.getPort());
//
//            }
//
//        }
//    }

    @Test
    void discoveryClientTest(){
        for (String service : discoveryClient.getServices()) {
            System.out.println(service);
            //获取实例IP，port
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                System.out.println("ip"+instance.getHost()+"port"+instance.getPort());
            }
        }

    }
}
