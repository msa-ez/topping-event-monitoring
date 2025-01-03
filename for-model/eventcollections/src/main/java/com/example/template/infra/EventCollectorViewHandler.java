path: /src/main/java/{{options.package}}/infra
---
package {{options.package}}.infra;

import {{options.package}}.config.kafka.KafkaProcessor;
import {{options.package}}.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EventCollectorViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private EventCollectorRepository eventCollectorRepository;

    private final Validator validator;

    public EventCollectorViewHandler() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private String getCorrelationKey(JsonNode jsonNode, String eventType) {
        switch (eventType) {
{{#boundedContexts}}
{{#each attached}}
{{#if (isEvent _type name)}}
    {{#each fieldDescriptors}}
        {{#if isCorrelationKey}}
            case "{{../namePascalCase}}":
            {{#if (checkPrimitiveType className)}}
                return jsonNode.get("{{name}}").asText();
            {{else}}
                {{#parseCorrelationKey className ../../../attached}}{{/parseCorrelationKey}}
            {{/if}}
        {{/if}}
    {{/each}}
{{/if}}
{{/each}}
{{/boundedContexts}}
            default:
                return "Unknown";
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void onEventReceived(@Payload String rawPayload) {
        try {
            // _type 속성 파싱하여 대상 클래스 타입 결정
            ObjectMapper typeMapper = new ObjectMapper();
            JsonNode jsonNode = typeMapper.readTree(rawPayload);

            // _type 속성 추출
            String type = jsonNode.get("eventType").asText();

            // 대상 클래스 동적 로드
            Class<?> eventClass = Class.forName("{{options.package}}.domain." + type);

            // 페이로드를 대상 클래스에 역직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            Object event = objectMapper.readValue(rawPayload, eventClass);

            // 이벤트 검증
            Set<ConstraintViolation<Object>> violations = validator.validate(event);
            if (violations.isEmpty()) {
                EventCollector eventCollector = new EventCollector();
                eventCollector.setType(type);
                eventCollector.setCorrelationKey(getCorrelationKey(jsonNode, type));
                eventCollector.setPayload(rawPayload);
                eventCollector.setTimestamp(jsonNode.get("timestamp").asLong());
{{#boundedContexts}}
    {{#each attached}}
        {{#if (isEvent _type name)}}
            {{#each fieldDescriptors}}
                {{#if isSearchKey}}
                eventCollector.set{{namePascalCase}}("Unknown");
                {{/if}}
            {{/each}}
        {{/if}}
    {{/each}}
{{/boundedContexts}}

                // 유효한 이벤트 저장
                eventCollectorRepository.save(eventCollector);
            } else {
                throw new Exception("Invalid event: " + violations);
            }
        } catch (Exception e) {
            try {
                // 필드 검증 비활성화 후 재파싱 시도
                ObjectMapper relaxedMapper = new ObjectMapper();
                relaxedMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // 다시 파싱하여 가능한 데이터 추출
                JsonNode jsonNode = new ObjectMapper().readTree(rawPayload);
                String type = jsonNode.get("eventType").asText();

                // 부분 데이터로 EventCollector 생성
                EventCollector eventCollector = new EventCollector();
                eventCollector.setType(type);
                eventCollector.setPayload(rawPayload);
                eventCollector.setCorrelationKey(getCorrelationKey(jsonNode, type));
                eventCollector.setTimestamp(jsonNode.has("timestamp") ? jsonNode.get("timestamp").asLong() : System.currentTimeMillis());
                eventCollector.setError(e.getMessage());
{{#boundedContexts}}
    {{#each attached}}
        {{#if (isEvent _type name)}}
            {{#each fieldDescriptors}}
                {{#if isSearchKey}}
                eventCollector.set{{namePascalCase}}("Unknown");
                {{/if}}
            {{/each}}
        {{/if}}
    {{/each}}
{{/boundedContexts}}

                // 부분 데이터 저장
                eventCollectorRepository.save(eventCollector);
            } catch (Exception innerException) {
                // 재파싱도 실패한 경우 처리
                EventCollector eventCollector = new EventCollector();
                eventCollector.setType("UnknownType");
                eventCollector.setPayload(rawPayload);
                eventCollector.setCorrelationKey("Unknown");
                eventCollector.setTimestamp(System.currentTimeMillis());
                eventCollector.setError(innerException.getMessage());
{{#boundedContexts}}
    {{#each attached}}
        {{#if (isEvent _type name)}}
            {{#each fieldDescriptors}}
                {{#if isSearchKey}}
                eventCollector.set{{namePascalCase}}("Unknown");
                {{/if}}
            {{/each}}
        {{/if}}
    {{/each}}
{{/boundedContexts}}

                // 최종 데이터 저장
                eventCollectorRepository.save(eventCollector);

                innerException.printStackTrace();
            }

            // 원래 예외 로그 기록
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}

<function>
var eventList = [];

window.$HandleBars.registerHelper('isEvent', function (type, name) {
    if (type.endsWith("Event") && !eventList.includes(name)) {
        eventList.push(name);
        return true;
    } else {
        return false;
    }
});

window.$HandleBars.registerHelper('checkPrimitiveType', function (type) {
    var primitiveTypes = ["String", "Integer", "Long", "Double", "Float", "Boolean", "Date", "BigDecimal"];
    if (primitiveTypes.includes(type)) {
        return true;
    } else {
        return false;
    }
});

window.$HandleBars.registerHelper('parseCorrelationKey', function (className, attached) {
    var text = "";
    if (attached.length > 0) {
        for (var i = 0; i < attached.length; i++) {
            if (attached[i] && attached[i].name && attached[i].name === className) {
                if (attached[i].isVO) {
                    var vo = attached[i];
                    if (vo.fieldDescriptors && vo.fieldDescriptors.length > 0) {
                        text = "return jsonNode.get(" + className + ").get(" + vo.fieldDescriptors[0].name + ").asText();";
                    }
                }
            }
        }
    }
    return text;
});
</function>