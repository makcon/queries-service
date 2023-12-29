package makcon.queries.adapters.config

import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class AdaptersConfig {
    
    @Bean
    fun template(template: NamedParameterJdbcTemplate) = NamedParameterJdbcTemplateExtensions(template)
}