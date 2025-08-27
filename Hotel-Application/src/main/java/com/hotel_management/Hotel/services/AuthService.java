package com.hotel_management.Hotel.services;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/dev")
public class AuthService {


    @GetMapping ("/code")
    public String code (){
        return  "You can see the code : \n " +
                "package com.hotel_management.Hotel.config;\n" +
                "\n" +
                "import com.hotel_management.Hotel.services.Custom.CustomUserDetailsService;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.security.authentication.AuthenticationProvider;\n" +
                "import org.springframework.security.authentication.dao.DaoAuthenticationProvider;\n" +
                "import org.springframework.security.config.Customizer;\n" +
                "import org.springframework.security.config.annotation.web.builders.HttpSecurity;\n" +
                "import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;\n" +
                "import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;\n" +
                "import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;\n" +
                "import org.springframework.security.web.SecurityFilterChain;\n" +
                "\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "public class SecurityConfig {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private CustomUserDetailsService customUserDetailsService;\n" +
                "\n" +
                "    @Autowired\n" +
                "    private AppConfig appConfig;\n" +
                "\n" +
                "    @Bean\n" +
                "    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {\n" +
                "\n" +
                "        http\n" +
                "                .csrf(AbstractHttpConfigurer :: disable)\n" +
                "                .authorizeHttpRequests(auth -> auth\n" +
                "                        .requestMatchers(\"/public/**\", \"/users/register\", \"/users/login\").permitAll()\n" +
                "                        .requestMatchers(\"/booking/**\", \"/profile/**\").hasRole(\"USER\")\n" +
                "                        .requestMatchers(\"/staff/**\", \"/checkin/**\",\n" +
                "                                \"/checkout/**\").hasRole(\"STAFF\")\n" +
                "                        .requestMatchers(\"/dev/**\").hasRole(\"DEV\")\n" +
                "                        .requestMatchers(\"/admin/**\", \"/reports/**\", \"/manSTAFFage/**\").hasRole(\"OWNER\")\n" +
                "                        .requestMatchers(\"/dashboard/**\").hasAnyRole(\"STAFF\", \"OWNER\")\n" +
                "                        .anyRequest().authenticated()     // allow all requests\n" +
                "                )\n" +
                "                .formLogin(Customizer.withDefaults())\n" +
                "                .httpBasic(Customizer.withDefaults());\n" +
                "        return http.build();\n" +
                "    }\n" +
                "}\n";
    }
}
