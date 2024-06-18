package fr.arthurrousseau.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.domain.JavaParameter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.springframework.stereotype.Controller;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "fr.arthurrousseau.archunit")
class ControllerTest {

    @ArchTest
    public static final ArchRule CONTROLLER_NAMING_RULE = classes().that()
            .areAnnotatedWith(Controller.class).should().haveSimpleNameEndingWith("Controller")
            .because("Les classes annotées @Controller doivent être suffixées par le mot clé \"Controller\"");

    @ArchTest
    public static final ArchRule CONTROLLER_ANNOTATION_RULE = classes().that()
            .haveSimpleNameEndingWith("Controller").should().beAnnotatedWith(Controller.class)
            .because("Les classes suffixées par le mot clé \"Controller\" doivent être annotées @Controller");

    @ArchTest
    public static final ArchRule CONTROLLER_LOCATION_RULE = classes().that()
            .areAnnotatedWith(Controller.class)
            .should().resideInAPackage("..controller")
            .because("Les classes annotées @Controller doivent être dans le package \"..controller\"");

    @ArchTest
    public static final ArchRule CONTROLLER_RULE = classes().that()
            .areAnnotatedWith(Controller.class).or().haveSimpleNameEndingWith("Controller")
            .should(new UseDtoObjectsOnly())
            .because("Les controllers doivent respecter les règles indiquées");

    /**
     * Condition qui permet de vérifier que l'ensemble des méthodes publiques d'une classe
     * manipule exclusivement des objets Dto.
     */
    static class UseDtoObjectsOnly extends ArchCondition<JavaClass> {
        public UseDtoObjectsOnly(Object... args) {
            super("use DTO objects only", args);
        }

        @Override
        public void check(JavaClass controllerClass, ConditionEvents events) {
            for (JavaMethod method : controllerClass.getMethods()) {
                if (!method.getModifiers().contains(JavaModifier.PUBLIC)) {
                    continue;
                }

                JavaClass returnClass = method.getReturnType().toErasure();

                // On vérifie le type de retour
                if (!isValidType(returnClass)) {
                    String message = String.format(
                            "Method %s.%s() returns %s which is not in the controller.model package and / or does not end with Dto",
                            controllerClass.getName(),
                            method.getName(),
                            returnClass.getName());
                    events.add(SimpleConditionEvent.violated(method, message));
                }

                for (JavaParameter parameter : method.getParameters()) {
                    var parameterClass = parameter.getType().toErasure();
                    if (!isValidType(parameterClass)) {
                        String message = String.format(
                                "Method %s.%s() has a parameter %s which is not in the controller.model package and / or does not end with Dto",
                                controllerClass.getName(),
                                method.getName(),
                                parameterClass.getName());
                        events.add(SimpleConditionEvent.violated(method, message));
                    }
                }
            }
        }

        /**
         * Méthode permettant de vérifier si le type fourni en entrée est valide.
         * Un type valide est un type qui :
         * - ne fait pas parti du projet ou
         * - fait parti du projet, se trouve dans le package "controller.model" et termine par "Dto"
         *
         * @return Vrai si le type fourni en entrée est valide, faux sinon
         */
        private boolean isValidType(JavaClass javaClass) {
            var packageName = javaClass.getPackageName();

            // On ne filtre que les types de notre projet
            if (!packageName.startsWith("fr.arthurrousseau.archunit")) {
                return true;
            }

            boolean isInCorrectPackage = packageName.contains("controller.model");
            boolean hasValidNaming = javaClass.getSimpleName().endsWith("Dto");

            return isInCorrectPackage && hasValidNaming;
        }
    }




}
