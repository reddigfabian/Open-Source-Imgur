package com.fret.anvilcodegen

import com.fret.di.ContributesViewModel
import com.google.auto.service.AutoService
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.compiler.api.*
import com.squareup.anvil.compiler.internal.asClassName
import com.squareup.anvil.compiler.internal.buildFile
import com.squareup.anvil.compiler.internal.fqName
import com.squareup.anvil.compiler.internal.reference.ClassReference
import com.squareup.anvil.compiler.internal.reference.asClassName
import com.squareup.anvil.compiler.internal.reference.asTypeName
import com.squareup.anvil.compiler.internal.reference.classAndInnerClassReferences
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.multibindings.IntoMap
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
        private val viewModelFactoryFqName = FqName("com.fret.gallery_list.impl.ViewModelFactoryPlugin")
        private val viewModelKeyFqName = FqName("com.fret.gallery_list.impl.ViewModelKey")
    }

    override fun isApplicable(context: AnvilContext): Boolean = true

    override fun generateCode(codeGenDir: File, module: ModuleDescriptor, projectFiles: Collection<KtFile>): Collection<GeneratedFile> {
        return projectFiles.classAndInnerClassReferences(module)
            .filter { it.isAnnotatedWith(ContributesViewModel::class.fqName) }
            .flatMap { listOf(generateModule(it, codeGenDir, module), generateAssistedFactory(it, codeGenDir, module)) }
            .toList()
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
//    override fun isApplicable(context: AnvilContext): Boolean = true
//
//    override fun generateCode(codeGenDir: File, module: ModuleDescriptor, projectFiles: Collection<KtFile>): Collection<GeneratedFile> {
//        return projectFiles.classAndInnerClassReferences(module)
//            .filter { it.isAnnotatedWith(ContributesViewModel::class.fqName) }
//            .flatMap { generate(it, codeGenDir, module) }
//            .toList()
//    }
//
//    private fun generate(vmClass: ClassReference.Psi, codeGenDir: File, module: ModuleDescriptor): Collection<GeneratedFile> {
//        val result = mutableListOf<GeneratedFile>()
//        val injectConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(Inject::class.fqName) }
//        val assistedInjectConstructor = vmClass.constructors.singleOrNull { it.isAnnotatedWith(AssistedInject::class.fqName) }
//        val assistedParameters = assistedInjectConstructor?.parameters?.filter { it.isAnnotatedWith(Assisted::class.fqName) }
//        if (injectConstructor == null && assistedInjectConstructor == null) {
//            throw AnvilCompilationException(
//                "${vmClass.fqName} must have either an @Inject constructor or an @AssistedInject constructor in order to use ${ContributesViewModel::class.fqName} annotation",
//                element = vmClass.clazz,
//            )
//        }
//
//        if (assistedInjectConstructor != null && assistedParameters.isNullOrEmpty()) {
//            throw AnvilCompilationException(
//                "${vmClass.fqName} must have at least one @Assisted parameter in order to use an @AssistedInject constructor with ${ContributesViewModel::class.fqName} annotation",
//                element = vmClass.clazz,
//            )
//        }
//        val isAssisted = assistedInjectConstructor != null
//
//        result.add(generateModule(vmClass, codeGenDir, module, isAssisted))
//        if (isAssisted) {
//            result.add(generateFactory(vmClass, codeGenDir, module, isAssisted))
//        }
//
//        return result
//    }
//
//    private fun generateModule(
//        vmClass: ClassReference.Psi,
//        codeGenDir: File,
//        module: ModuleDescriptor,
//        isAssisted: Boolean
//    ): GeneratedFile {
//        val generatedPackage = vmClass.packageFqName.toString()
//        val moduleClassName = "${vmClass.shortName}_Module"
//        val scope = vmClass.annotations
//            .single { it.fqName == ContributesViewModel::class.fqName }
//            .scope()
//        val content = FileSpec.buildFile(generatedPackage, moduleClassName) {
//            addType(
//                TypeSpec.classBuilder(moduleClassName)
//                    .addModifiers(KModifier.ABSTRACT)
//                    .addAnnotation(Module::class)
//                    .addAnnotation(AnnotationSpec.builder(ContributesTo::class).addMember("%T::class", scope.asClassName()).build())
//                    .addFunction(
//                        FunSpec.builder("bind${vmClass.shortName}Factory")
//                            .addModifiers(KModifier.ABSTRACT)
//                            .addParameter("factory", ClassName(generatedPackage, "${vmClass.shortName}_${if (isAssisted) "Assisted" else ""}Factory"))
//                            .returns(viewModelFactoryFqName.asClassName(module))
//                            .addAnnotation(Binds::class)
//                            .addAnnotation(IntoMap::class)
//                            .addAnnotation(AnnotationSpec.builder(viewModelKeyFqName.asClassName(module)).addMember("%T::class", vmClass.asClassName()).build())
//                            .build(),
//                    )
//                    .build(),
//            )
//        }
//        return createGeneratedFile(codeGenDir, generatedPackage, moduleClassName, content)
//    }
//
//    private fun generateFactory(
//        vmClass: ClassReference.Psi,
//        codeGenDir: File,
//        module: ModuleDescriptor,
//        isAssisted: Boolean
//    ): GeneratedFile {
//        val generatedPackage = vmClass.packageFqName.toString()
//        val assistedFactoryClassName = "${vmClass.shortName}_AssistedFactory"
//        val vmClassName = vmClass.asClassName()
//        val content = FileSpec.buildFile(generatedPackage, assistedFactoryClassName) {
//            addType(
//                TypeSpec.interfaceBuilder(assistedFactoryClassName)
//                    .addSuperinterface(viewModelFactoryFqName.asClassName(module))
//                    .addAnnotation(AssistedFactory::class)
//                    .addFunction(
//                        FunSpec.builder("create")
//                            .addModifiers(KModifier.OVERRIDE, KModifier.ABSTRACT)
//                            .returns(vmClassName)
//                            .build(),
//                    )
//                    .build(),
//            )
//        }
//        return createGeneratedFile(codeGenDir, generatedPackage, assistedFactoryClassName, content)
//    }
