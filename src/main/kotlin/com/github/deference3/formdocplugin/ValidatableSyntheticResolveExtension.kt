package com.github.deference3.formdocplugin

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeUtils
import org.jetbrains.kotlin.types.typeUtil.asTypeProjection
//import org.jetbrains.kotlin.idea.extensions.SyntheticResolveExtension

class ValidatableSyntheticResolveExtension : FirD {

    // FQN of your annotation
    private val validatableAnnotationFqName = FqName("com.example.Validatable")

    override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> {
        // Only classes annotated with @Validatable get the metadata function
        if (thisDescriptor.annotations.hasAnnotation(validatableAnnotationFqName)) {
            return listOf(Name.identifier("metadata"))
        }
        return emptyList()
    }

    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (name.asString() == "metadata" && thisDescriptor.annotations.hasAnnotation(validatableAnnotationFqName)) {
            // Create the synthetic function descriptor
            val metadataFunction = SimpleFunctionDescriptorImpl.create(
                containingDeclaration = thisDescriptor,
                annotations = Annotations.EMPTY,
                name = name,
                kind = CallableMemberDescriptor.Kind.SYNTHESIZED,
                source = thisDescriptor.source
            ).apply {
                initialize(
                    receiverParameter = null,
                    dispatchReceiverParameter = thisDescriptor.thisAsReceiverParameter,
                    typeParameters = emptyList(),
                    unsubstitutedValueParameters = emptyList(),
                    unsubstitutedReturnType = resolveMetadataReturnType(),
                    modality = Modality.FINAL,
                    visibility = Visibilities.PUBLIC
                )
            }
            result.add(metadataFunction)
        }
    }

    private fun resolveMetadataReturnType(): KotlinType {
        // Resolve your Metadata class type. This is just an example: use a known class.
        // You need to obtain the KotlinType for your Metadata class.
        // For simplicity, we create a dummy type.
        val metadataFqName = FqName("com.example.Metadata")
        val moduleDescriptor = DescriptorUtils.getContainingModule(metadataFqName) // pseudo-code, not real
        // In practice, you'd use bindingContext or module to find the class descriptor.
        // This is a placeholder; implement actual resolution.
        TODO("Resolve Metadata type from the project")
    }
}