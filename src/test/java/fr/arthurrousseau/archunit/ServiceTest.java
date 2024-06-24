package fr.arthurrousseau.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@AnalyzeClasses(packages = "fr.arthurrousseau.archunit")
public class ServiceTest {

    @ArchTest
    public static final ArchRule SERVICES_SHOULD_CALL_LOGGER_RULE = FreezingArchRule.freeze(methods().that()
            .areDeclaredInClassesThat(
                    DescribedPredicate.or(
                            HasName.Predicates.nameEndingWith("ServiceImpl"),
                            CanBeAnnotated.Predicates.annotatedWith(Service.class)
                    ))
            .should(new ArchCondition<>("call logger") {
                @Override
                public void check(JavaMethod javaMethod, ConditionEvents conditionEvents) {
                    var callsLogger = javaMethod.getCallsFromSelf().stream().anyMatch(it -> it.getTargetOwner().getName().equals("org.slf4j.Logger"));

                    if (!callsLogger) {
                        var message = String.format("Method %s.%s() doesn't log anything", javaMethod.getOwner().getName(), javaMethod.getName());

                        conditionEvents.add(SimpleConditionEvent.violated(javaMethod, message));
                    }
                }
            }));

    @ArchTest
    public static final ArchRule SERVICE_RULE = classes().that().areAnnotatedWith(Service.class).or().haveSimpleNameEndingWith("ServiceImpl")
            .should().resideInAPackage("..service.impl..")
            .andShould().haveSimpleNameEndingWith("ServiceImpl")
            .andShould().beAnnotatedWith(Service.class);

}
