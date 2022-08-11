package com.fret.anvilcodegen

import com.fret.di.ContributesViewModel
import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.compiler.api.*
import com.squareup.anvil.compiler.internal.asClassName
import com.squareup.anvil.compiler.internal.buildFile
import com.squareup.anvil.compiler.internal.fqName
import com.squareup.anvil.compiler.internal.reference.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.multibindings.IntoMap
import org.jetbrains.kotlin.backend.jvm.lower.varargPhase
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import java.io.File
import javax.inject.Inject

/**
 * This Anvil code generator allows you to @AssistedInject a ViewModel without registering it in a Dagger
 * Module by hand.
 */
@AutoService(CodeGenerator::class)
class ContributesViewModelCodeGenerator : CodeGenerator {
    companion object {
        private val viewModelFactoryFqNameExperimental = FqName("com.fret.utils.ViewModelFactoryExperimental")
        private val viewModelFactoryFqName = FqName("com.fret.utils.ViewModelFactory")
        private val viewModelKeyFqName = FqName("com.fret.utils.ViewModelKey")
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(codeGenDir: File, module: ModuleDescriptor, projectFiles: Collection<KtFile>): Collection<GeneratedFile> {
        return projectFiles.classAndInnerClassReferences(module)
            .filter { it.isAnnotatedWith(ContributesViewModel::class.fqName) }
            .flatMap { generate(it, codeGenDir, module) }
            .toList()
    }

    private fun generate(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor) : Collection<GeneratedFile> {
        val injectConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(Inject::class.fqName) }
        val assistedInjectConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(AssistedInject::class.fqName) }
        val assistedParameter = assistedInjectConstructor?.parameters?.singleOrNull { it.isAnnotatedWith(Assisted::class.fqName) }
        val isAssisted = assistedInjectConstructor != null

        val result = mutableListOf<GeneratedFile>()
//        result.add(generateModuleExperimental(vmClass, codeGenDir, module, isAssisted))
//        if (isAssisted) {
//            result.add(generateAssistedFactoryExperimental(vmClass, codeGenDir, module))
//        }
        result.add(generateModule(vmClass, codeGenDir, module))
        result.add(generateAssistedFactory(vmClass, codeGenDir, module))
        return result
    }

    private fun generateModuleExperimental(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor, isAssisted: Boolean): GeneratedFile {
        val injectConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(Inject::class.fqName) }
        val generatedPackage = vmClass.packageFqName.toString()
        val moduleClassName = "${vmClass.shortName}_Module_Experimental"
        val scope = vmClass.annotations.single { it.fqName == ContributesViewModel::class.fqName }.scope()

        val classBuilder = TypeSpec.classBuilder(moduleClassName)
            .addModifiers(KModifier.ABSTRACT)
            .addAnnotation(Module::class)
            .addAnnotation(
                AnnotationSpec.builder(ContributesTo::class)
                    .addMember("%T::class", scope.asClassName()).build()
            )
            .addFunction(
                FunSpec.builder("bind${vmClass.shortName}Factory_Experimental")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(
                        "factory",
                        ClassName(
                            generatedPackage,
                            "${vmClass.shortName}_${if (isAssisted) "AssistedFactory_Experimental" else "Factory_Experimental"}"
                        )
                    )
                    .returns(
                        viewModelFactoryFqNameExperimental.asClassName(module).parameterizedBy(STAR)
                    )
                    .addAnnotation(Binds::class)
                    .addAnnotation(IntoMap::class)
                    .addAnnotation(
                        AnnotationSpec.builder(viewModelKeyFqName.asClassName(module))
                            .addMember("%T::class", vmClass.asClassName()).build()
                    )
                    .build(),
            )


        if (!isAssisted) {
            val providesBuilder = FunSpec.builder("provides${vmClass.shortName}_Factory")
                .returns(
                    ClassName(
                        generatedPackage,
                        "${vmClass.shortName}_Factory"
                    ))
                .addAnnotation(Provides::class)

            val codeStringBuilder = StringBuilder("return ${vmClass.shortName}_Factory.newInstance(")
            injectConstructor?.parameters?.forEach { parameterReference ->
                providesBuilder.addParameter(
                    parameterReference.name,
                    parameterReference.type().asTypeName()
                )
                codeStringBuilder.append(parameterReference.name)
                codeStringBuilder.append(",")
            }
            codeStringBuilder.replace(codeStringBuilder.length -1, codeStringBuilder.length, ")")
            providesBuilder.addCode(codeStringBuilder.toString())
            classBuilder.addFunction(
                providesBuilder.build()
            )
        }

        val content = FileSpec.buildFile(generatedPackage, moduleClassName) {
            addType(
                classBuilder.build(),
            )
        }
        return createGeneratedFile(codeGenDir, generatedPackage, moduleClassName, content)
    }

    private fun generateAssistedFactoryExperimental(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor): GeneratedFile {
        val generatedPackage = vmClass.packageFqName.toString()
        val assistedFactoryClassName = "${vmClass.shortName}_AssistedFactory_Experimental"
        val assistedConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(AssistedInject::class.fqName) }
        val assistedParameters = assistedConstructor?.parameters?.filter { it.isAnnotatedWith(Assisted::class.fqName) }
        if (assistedConstructor == null || assistedParameters.isNullOrEmpty()) {
            throw AnvilCompilationException(
                "${vmClass.fqName} must have an @AssistedInject constructor with @Assisted parameters",
                element = vmClass.clazz,
            )
        }
        val vmClassName = vmClass.asClassName()
        val content = FileSpec.buildFile(generatedPackage, assistedFactoryClassName) {
            val createBuilder = FunSpec.builder("create")
                .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
                .returns(vmClassName)

            assistedParameters.forEach {
                createBuilder.addParameter(it.name, it.type().asTypeName())
            }

            addType(
                TypeSpec.interfaceBuilder(assistedFactoryClassName)
                    .addSuperinterface(viewModelFactoryFqNameExperimental.asClassName(module).parameterizedBy(vmClassName))
                    .addAnnotation(AssistedFactory::class)
                    .addFunction(
                        createBuilder.build(),
                    )
                    .build(),
            )
        }
        return createGeneratedFile(codeGenDir, generatedPackage, assistedFactoryClassName, content)
    }

    private fun generateModule(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor): GeneratedFile {
        val generatedPackage = vmClass.packageFqName.toString()
        val moduleClassName = "${vmClass.shortName}_Module"
        val scope = vmClass.annotations.single { it.fqName == ContributesViewModel::class.fqName }.scope()
        val content = FileSpec.buildFile(generatedPackage, moduleClassName) {
            addType(
                TypeSpec.classBuilder(moduleClassName)
                    .addModifiers(KModifier.ABSTRACT)
                    .addAnnotation(Module::class)
                    .addAnnotation(AnnotationSpec.builder(ContributesTo::class).addMember("%T::class", scope.asClassName()).build())
                    .addFunction(
                        FunSpec.builder("bind${vmClass.shortName}Factory")
                            .addModifiers(KModifier.ABSTRACT)
                            .addParameter("factory", ClassName(generatedPackage, "${vmClass.shortName}_AssistedFactory"))
                            .returns(viewModelFactoryFqName.asClassName(module).parameterizedBy(STAR, STAR))
                            .addAnnotation(Binds::class)
                            .addAnnotation(IntoMap::class)
                            .addAnnotation(AnnotationSpec.builder(viewModelKeyFqName.asClassName(module)).addMember("%T::class", vmClass.asClassName()).build())
                            .build(),
                    )
                    .build(),
            )
        }
        return createGeneratedFile(codeGenDir, generatedPackage, moduleClassName, content)
    }

    private fun generateAssistedFactory(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor): GeneratedFile {
        val generatedPackage = vmClass.packageFqName.toString()
        val assistedFactoryClassName = "${vmClass.shortName}_AssistedFactory"
        val constructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(AssistedInject::class.fqName) }
        val assistedParameter = constructor?.parameters?.singleOrNull { it.isAnnotatedWith(Assisted::class.fqName) }
        if (constructor == null || assistedParameter == null) {
            throw AnvilCompilationException(
                "${vmClass.fqName} must have an @AssistedInject constructor with @Assisted initialState: S parameter",
                element = vmClass.clazz,
            )
        }
        if (assistedParameter.name != "initialState") {
            throw AnvilCompilationException(
                "${vmClass.fqName} @Assisted parameter must be named initialState",
                element = assistedParameter.parameter,
            )
        }
        val vmClassName = vmClass.asClassName()
        val stateClassName = assistedParameter.type().asTypeName()
        val content = FileSpec.buildFile(generatedPackage, assistedFactoryClassName) {
            addType(
                TypeSpec.interfaceBuilder(assistedFactoryClassName)
                    .addSuperinterface(viewModelFactoryFqName.asClassName(module).parameterizedBy(vmClassName, stateClassName))
                    .addAnnotation(AssistedFactory::class)
                    .addFunction(
                        FunSpec.builder("create")
                            .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
                            .addParameter("initialState", stateClassName)
                            .returns(vmClassName)
                            .build(),
                    )
                    .build(),
            )
        }
        return createGeneratedFile(codeGenDir, generatedPackage, assistedFactoryClassName, content)
    }
}
