# Spring Cloud 微服务示例项目

## 项目介绍

这是一个基于Spring Cloud的微服务示例项目，演示了服务注册与发现、远程调用等核心功能。项目使用Nacos作为服务注册中心，并配置为集群模式。

## 技术栈

- Spring Boot 3.4.4
- Spring Cloud 2024.0.1
- Spring Cloud Alibaba 2023.0.3.2
- Nacos 3.0.0（服务注册与配置中心）
- OpenFeign（服务间调用）
- Sentinel（服务保护）
- Spring Cloud LoadBalancer（负载均衡）

## 项目结构

```
cloud-demo/
├── model/                 # 共享模型模块
├── services/              # 微服务模块
│   ├── service-product/   # 产品服务
│   └── service-order/     # 订单服务
└── pom.xml                # 父项目POM
```

## 功能特性

- 服务注册与发现：使用Nacos作为服务注册中心
- 远程服务调用：使用OpenFeign和RestTemplate实现服务间通信
- 负载均衡：集成Spring Cloud LoadBalancer，支持轮询和随机策略
- 服务保护：集成Sentinel实现服务熔断降级
- 配置中心：使用Nacos作为配置中心

## 实验内容与实现

### 1. 服务调用实现

#### 1.1 使用RestTemplate完成服务调用

实现了使用RestTemplate进行服务间调用的四种HTTP方法：

1. **GET方法**：获取产品信息
   ```java
   // 获取单个产品
   @GetMapping("/product/{id}")
   public Object getProduct(@PathVariable Long id) {
       return restTemplate.getForObject(PRODUCT_SERVICE_URL + "/product/" + id, Object.class);
   }
   
   // 获取所有产品
   @GetMapping("/products")
   public List<Object> getAllProducts() {
       ResponseEntity<List> response = restTemplate.getForEntity(PRODUCT_SERVICE_URL + "/products", List.class);
       return response.getBody();
   }
   ```

2. **POST方法**：创建新产品
   ```java
   @PostMapping("/product")
   public Object createProduct(@RequestBody Map<String, Object> product) {
       HttpEntity<Map<String, Object>> request = new HttpEntity<>(product);
       return restTemplate.postForObject(PRODUCT_SERVICE_URL + "/product", request, Object.class);
   }
   ```

3. **PUT方法**：更新产品
   ```java
   @PutMapping("/product/{id}")
   public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> product) {
       HttpEntity<Map<String, Object>> request = new HttpEntity<>(product);
       restTemplate.put(PRODUCT_SERVICE_URL + "/product/" + id, request);
       
       Object updatedProduct = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/product/" + id, Object.class);
       return ResponseEntity.ok(updatedProduct);
   }
   ```

4. **DELETE方法**：删除产品
   ```java
   @DeleteMapping("/product/{id}")
   public Object deleteProduct(@PathVariable Long id) {
       Object product = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/product/" + id, Object.class);
       
       Map<String, Object> params = new HashMap<>();
       params.put("id", id);
       restTemplate.delete(PRODUCT_SERVICE_URL + "/product/{id}", params);
       
       return product;
   }
   ```

#### 1.2 使用OpenFeign完成服务调用

实现了使用OpenFeign进行服务间调用：

1. **定义Feign接口**
   ```java
   @FeignClient(value = "service-product", fallback = ProductFeignClient.class)
   public interface ProductFenignClient {
       @GetMapping("/product/{id}")
       Product getProductById(@PathVariable("id") Long id);
   }
   ```

2. **实现服务降级**
   ```java
   @Component
   public class ProductFeignClient implements ProductFenignClient {
       @Override
       public Product getProductById(Long id) {
           System.out.println("兜底回调");
           return new Product();
       }
   }
   ```

3. **在服务中使用Feign客户端**
   ```java
   @Service
   public class orderServiceImpl implements orderService {
       @Autowired
       ProductFenignClient productFenignClient;
   
       @Override
       public Order createOrder(Long productId, Long userId) {
           Product product = productFenignClient.getProductById(productId);
           // 处理订单逻辑...
           return order;
       }
   }
   ```

### 2. 负载均衡实现

#### 2.1 默认轮询策略

使用Spring Cloud LoadBalancer的默认轮询策略进行负载均衡：

```java
private Product getProductFormRemoteWithAnnotation(Long productId){
    //使用注解这里会被动态替换：service-product
    String url="http://service-product/product/"+productId;
    log.info("远程请求{}",url);
    return restTemplate.getForObject(url, Product.class);
}
```

#### 2.2 随机负载均衡策略

实现了随机负载均衡策略：

1. **配置随机负载均衡器**
   ```java
   @Configuration
   public class LoadBalancerConfig {
       @Bean
       public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                    LoadBalancerClientFactory loadBalancerClientFactory) {
           String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
           return new RandomLoadBalancer(loadBalancerClientFactory
                   .getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                   serviceId);
       }
   }
   ```

2. **将随机策略应用到特定服务**
   ```java
   @Configuration
   @LoadBalancerClient(value = "service-product", configuration = LoadBalancerConfig.class)
   public class LoadBalancerClientConfig {
       // 此类只需要注解配置，无需其他代码
   }
   ```

3. **测试负载均衡效果**
   ```java
   @RestController
   @RequestMapping("/loadbalancer")
   public class LoadBalancerTestController {
       @Autowired
       private LoadBalancerClient loadBalancerClient;
       
       @GetMapping("/manual")
       public Map<String, Object> testManualLoadBalancer() {
           // 使用LoadBalancerClient选择服务实例（此时会使用随机策略）
           ServiceInstance instance = loadBalancerClient.choose("service-product");
           // 返回选中的实例信息...
       }
       
       @GetMapping("/auto")
       public String testAutoLoadBalancer() {
           // 使用@LoadBalanced注解的RestTemplate自动进行负载均衡
           String url = "http://service-product/instance-info";
           return restTemplate.getForObject(url, String.class);
       }
   }
   ```

### 3. 多服务提供者集群

创建了多个服务提供者实例，使用不同的端口号：

1. **第一个实例**：端口8089
2. **第二个实例**：端口8090

通过在启动时指定不同的配置文件或端口参数来启动多个实例：
```
java -jar service-product.jar --server.port=8089
java -jar service-product.jar --server.port=8090
```

### 4. OpenFeign与RestTemplate对比分析

#### 4.1 调用方式对比

- **RestTemplate**：命令式，需要手动构建请求
  ```java
  String url = "http://service-product/product/" + id;
  Product product = restTemplate.getForObject(url, Product.class);
  ```

- **OpenFeign**：声明式，接口定义即调用
  ```java
  Product product = productFeignClient.getProductById(id);
  ```

#### 4.2 OpenFeign的主要优势

1. **声明式API**：通过接口+注解定义服务调用，代码更简洁
2. **与Spring MVC注解兼容**：使用相同的注解，降低学习成本
3. **自动集成负载均衡**：无需额外配置即可实现负载均衡
4. **优雅的服务降级**：与Sentinel/Hystrix无缝集成
5. **请求压缩**：支持GZIP压缩，减少网络传输量
6. **灵活的配置**：支持全局和针对特定服务的配置
7. **日志支持**：内置四种日志级别，方便调试

在Spring Cloud微服务架构中，OpenFeign相比RestTemplate具有明显的优势，特别是在代码简洁性、可维护性和功能丰富性方面。它使得微服务间的调用就像调用本地方法一样简单，大大降低了开发难度和维护成本。

## 快速开始

### 前置条件

- JDK 17+
- Maven 3.6+
- Nacos 3.0.0+

### 启动步骤

1. 启动Nacos服务（集群模式）

   ```
   cd nacos/bin
   startup.cmd -m cluster
   ```
2. 启动产品服务（多实例）

   ```
   # 启动第一个产品服务实例
   cd services/service-product
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8089

   # 启动第二个产品服务实例
   cd services/service-product
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8090
   ```

3. 启动订单服务

   ```
   cd services/service-order
   mvn spring-boot:run
   ```

4. 访问服务

   - 产品服务：http://localhost:8089/product/{id} 或 http://localhost:8090/product/{id}
   - 订单服务：http://localhost:8095/order?productId=1&userId=1
   - RestTemplate调用：http://localhost:8095/rest/product/{id}
   - 负载均衡测试：http://localhost:8095/loadbalancer/manual

## 测试API

可以使用以下HTTP请求测试项目功能：

```http
### 1. 直接调用产品服务 - GET 获取单个产品
GET http://localhost:8089/product/1

### 2. 通过订单服务的RestTemplate调用 - GET 获取产品
GET http://localhost:8095/rest/product/1

### 3. 通过订单服务的RestTemplate调用 - POST 创建产品
POST http://localhost:8095/rest/product
Content-Type: application/json

{
  "productName": "RestTemplate创建的产品",
  "price": 399.99,
  "sum": 200
}

### 4. 通过订单服务的OpenFeign调用 - GET 获取产品
GET http://localhost:8095/order?productId=1&userId=1

### 5. 测试负载均衡 - 随机策略
GET http://localhost:8095/loadbalancer/manual
```

## Nacos集群配置

项目使用Nacos集群模式，配置了外部MySQL数据库存储配置和服务信息，确保高可用性。

## 项目说明

本项目展示了Spring Cloud微服务架构的基本组件和使用方式，包括服务注册与发现、远程调用、负载均衡和服务降级等核心功能。


