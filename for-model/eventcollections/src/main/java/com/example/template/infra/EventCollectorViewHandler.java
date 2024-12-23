path: /src/main/java/{{options.package}}/infra
---
package {{options.package}}.infra;

import {{options.package}}.config.kafka.KafkaProcessor;
import {{options.package}}.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EventCollectorViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private EventCollectorRepository eventCollectorRepository;

{{#boundedContexts}}
    {{#each attached}}
    {{#if (isEvent _type name)}}
    @StreamListener(KafkaProcessor.INPUT)
    public void when{{namePascalCase}}_then_CREATE(
        @Payload {{namePascalCase}} {{nameCamelCase}}
    ) {
        try {
            if (!{{nameCamelCase}}.validate()) return;

            // view 객체 생성
            EventCollector eventCollector = new EventCollector();
            // view 객체에 이벤트의 Value 를 set 함
            eventCollector.setType({{nameCamelCase}}.getEventType());
            eventCollector.setCorrelationKey(
                {{#checkCorrelationKey nameCamelCase fieldDescriptors}}{{/checkCorrelationKey}}
            );
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString({{nameCamelCase}});
            eventCollector.setPayload(jsonPayload);
            eventCollector.setTimestamp({{nameCamelCase}}.getTimestamp());
            {{#setSearchKey nameCamelCase fieldDescriptors}}{{/setSearchKey}}
            // view 레파지 토리에 save
            eventCollectorRepository.save(eventCollector);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    {{/if}}
    {{/each}}
{{/boundedContexts}}
    //>>> DDD / CQRS
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

window.$HandleBars.registerHelper('checkCorrelationKey', function (eventName, fieldDescriptors) {
    var text = "";
    for(var i = 0; i < fieldDescriptors.length; i ++ ){
        if(fieldDescriptors[i] && fieldDescriptors[i].isCorrelationKey) {
            const value = eventName + ".get" + fieldDescriptors[i].namePascalCase + "()";
            if (fieldDescriptors[i].className == "String") {
                text += value;
            } else {
                text += "String.valueOf(" + value + ")"
            }
            return text;
        }
    }
});

window.$HandleBars.registerHelper('setSearchKey', function (eventName, fieldDescriptors) {
    var text = "";
    for(var i = 0; i < fieldDescriptors.length; i ++ ) {
        if(fieldDescriptors[i] && fieldDescriptors[i].isSearchKey && !searchKeyList.includes(fieldDescriptors[i].name)) {
            searchKeyList.push(fieldDescriptors[i].name);
            const value = eventName + ".get" + fieldDescriptors[i].namePascalCase + "()";
            text += "eventCollector.set" + fieldDescriptors[i].namePascalCase + "(" + value + ");"
        }
    }
    return text;
});
</function>