package fr.arthurrousseau.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

// Si vous utilises JUnit 4, pensez à utiliser le runner ArchUnitRunner via l'annotation @RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "fr.arthurrousseau.archunit")
class ArchitectureTest {

    @ArchTest
    public static final ArchRule CLASSES_IN_SERVICE_PACKAGE_MUST_BE_ANNOTATED = classes().that().resideInAPackage("..service.impl").should().beAnnotatedWith(Service.class);

    @ArchTest
    public static final ArchRule LAYERED_ARCHITECTURE_TEST = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Data").definedBy("..data..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Controller").mayOnlyAccessLayers("Service")
            .whereLayer("Service").mayNotAccessAnyLayer()
            .whereLayer("Data").mayOnlyBeAccessedByLayers("Service");

    /**
     * Méthode permettant de tester que l'ensemble des classes présentes dans le package "[...].service.impl"
     * sont bien annotées avec l'annotation @Service
     *
     * @param classes Classes chargées via l'annotation @AnalyzeClasses
     */
    @ArchTest
    void testThatClassesInServiceImplPackageMustBeAnnotated(JavaClasses classes) {
        var rule = classes().that().resideInAPackage("..service.impl").should().beAnnotatedWith(Service.class);
        rule.check(classes);
    }

}
