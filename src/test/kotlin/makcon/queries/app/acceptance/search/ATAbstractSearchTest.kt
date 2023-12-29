package makcon.queries.app.acceptance.search

import com.fasterxml.jackson.databind.ObjectMapper
import makcon.queries.app.acceptance.ATAbstractTest
import makcon.queries.app.mapper.toDto
import makcon.queries.app.mother.CreateObjectCommandMother
import makcon.queries.app.mother.OrderDataMother
import makcon.queries.app.mother.ProductDataMother
import makcon.queries.app.mother.UserDataMother
import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.UserData
import makcon.queries.domain.port.OrderRepositoryPort
import makcon.queries.domain.port.ProductRepositoryPort
import makcon.queries.domain.port.UserRepositoryPort
import makcon.queries.dto.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.reflect.full.memberProperties

@AutoConfigureMockMvc
abstract class ATAbstractSearchTest<T> : ATAbstractTest() {
    
    @Autowired
    lateinit var mvc: MockMvc
    
    @Autowired
    lateinit var objectMapper: ObjectMapper
    
    @Autowired
    lateinit var userRepository: UserRepositoryPort
    
    @Autowired
    lateinit var orderRepository: OrderRepositoryPort
    
    @Autowired
    lateinit var productRepository: ProductRepositoryPort
    
    fun createUser(
        id: String = Rand.uuidStr(),
        data: UserData = UserDataMother.of(),
    ): UserV1 {
        val command = CreateObjectCommandMother.of(objectId = id, data = data)
        userRepository.create(command)
        
        return userRepository.findById(command.objectId)!!.toDto()
    }
    
    fun createProduct(
        id: String = Rand.uuidStr(),
        data: ProductData = ProductDataMother.of(),
    ): ProductV1 {
        val command = CreateObjectCommandMother.of(objectId = id, data = data)
        productRepository.create(command)
        
        return productRepository.findById(command.objectId)!!.toDto()
    }
    
    fun createOrder(
        id: String = Rand.uuidStr(),
        data: OrderData = OrderDataMother.of(),
    ): OrderV1 {
        createUser(id = data.userId)
        createProduct(id = data.productId)
        
        val command = CreateObjectCommandMother.of(objectId = id, data = data)
        orderRepository.create(command)
        
        return orderRepository.findById(command.objectId)!!.toDto()
    }
    
    fun findUser(id: String) = userRepository.findById(id)!!.toDto()
    
    fun findProduct(id: String) = productRepository.findById(id)!!.toDto()
    
    fun searchAndGetResponse(query: QueryV1, urlPath: String, itemsType: Class<T>): SearchResultV1<T> {
        val request = MockMvcRequestBuilders
            .post("/v1/$urlPath")
            .content(objectMapper.writeValueAsString(query))
            .contentType(MediaType.APPLICATION_JSON)
        
        val response = mvc.perform(request)
            .andReturn()
            .response
            .contentAsString
        
        val responseType = objectMapper.typeFactory.constructParametricType(
            SearchResultV1::class.java,
            itemsType
        )
        
        return objectMapper.readValue(response, responseType)
    }
    
    fun searchAndGetItems(query: QueryV1, urlPath: String, itemsType: Class<T>): List<T> =
        searchAndGetResponse(query, urlPath, itemsType).items!!
    
    fun Any.getValue(field: String) = this::class
        .memberProperties
        .find { it.name == field }!!
        .getter.call(this)!!
}