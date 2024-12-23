path: /src/main/java/{{options.package}}/domain
---
package {{options.package}}.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "EventCollector_table")
@Data
public class EventCollector {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String type;
    private String correlationKey;
    
    @Lob
    private String payload;
    private Long timestamp;
{{#boundedContexts}}
{{#each attached}}
{{#if (isEvent _type name)}}
    {{#setSearchKey fieldDescriptors}}{{/setSearchKey}}
{{/if}}
{{/each}}
{{/boundedContexts}}
}

<function>
var eventList = [];
var searchKeyList = [];

window.$HandleBars.registerHelper('isEvent', function (type, name) {
    if (type.endsWith("Event") && !eventList.includes(name)) {
        eventList.push(name);
        return true;
    } else {
        return false;
    }
});

window.$HandleBars.registerHelper('setSearchKey', function (fieldDescriptors) {
    var text = "";
    for(var i = 0; i < fieldDescriptors.length; i ++ ){
        if(fieldDescriptors[i] && fieldDescriptors[i].isSearchKey && !searchKeyList.includes(fieldDescriptors[i].name)) {
            searchKeyList.push(fieldDescriptors[i].name);
            text += "private " + fieldDescriptors[i].className + " " + fieldDescriptors[i].nameCamelCase + ";"
        }
    }
    return text;
});
</function>