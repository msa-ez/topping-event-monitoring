forEach: Event
representativeFor: Event
fileName: {{namePascalCase}}.java
path: eventcollections/src/main/java/{{options.package}}/domain
priority: 1
---
package {{options.package}}.domain;

import {{options.package}}.infra.AbstractEvent;
import java.util.*;
import javax.validation.constraints.*;
import lombok.*;
{{#checkBigDecimal fieldDescriptors}}{{/checkBigDecimal}}

//<<< DDD / Domain Event
@Data
public class {{namePascalCase}} extends AbstractEvent {

    {{#fieldDescriptors}}
    {{#if (or isCorrelationKey isSearchKey)}}
    @NotNull
    {{/if}}
    private {{{className}}} {{camelCase nameCamelCase}};
    {{/fieldDescriptors}}

}
//>>> DDD / Domain Event
<function>
window.$HandleBars.registerHelper('checkBigDecimal', function (fieldDescriptors) {
    for(var i = 0; i < fieldDescriptors.length; i ++ ){
        if(fieldDescriptors[i] && fieldDescriptors[i].className.includes('BigDecimal')){
            return "import java.math.BigDecimal;";
        }
    }
});

window.$HandleBars.registerHelper('or', function (a, b) {
    return a || b;
});
</function>