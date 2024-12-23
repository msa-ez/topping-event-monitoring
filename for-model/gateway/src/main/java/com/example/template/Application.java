path: /src/main/java/{{options.package}}
---
package {{options.package}};
{{#if (isSelectedSecurity options.rootModel.toppingPlatforms)}}

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
public class Application {

    public static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);
    }


}
{{else}}
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);
    }


}
{{/if}}


<function>
window.$HandleBars.registerHelper('isSelectedSecurity', function (toppingPlatforms) {
    var selectedSecurity = false;
    try{
        for(var i=0; i<toppingPlatforms.length; i++){
            if(toppingPlatforms[i] == "keycloak-security" || toppingPlatforms[i] == "spring-security"){
                selectedSecurity =  true;
            }
        }

        return selectedSecurity;

    } catch(e){
        console.log(e)
    }
});
</function>
