package com.patrykandpatryk.liftapp.domain.di

import com.patrykandpatryk.liftapp.domain.model.StringResource
import com.patrykandpatryk.liftapp.domain.serialization.LocalDateSerializer
import com.patrykandpatryk.liftapp.domain.serialization.PolymorphicEnumSerializer
import com.patrykandpatryk.liftapp.domain.unit.LongDistanceUnit
import com.patrykandpatryk.liftapp.domain.unit.MassUnit
import com.patrykandpatryk.liftapp.domain.unit.MediumDistanceUnit
import com.patrykandpatryk.liftapp.domain.unit.PercentageUnit
import com.patrykandpatryk.liftapp.domain.unit.ShortDistanceUnit
import com.patrykandpatryk.liftapp.domain.unit.ValueUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

typealias StringResourceSerializers =
    Set<@JvmSuppressWildcards Pair<KClass<StringResource>, KSerializer<StringResource>>>

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    companion object {
        @Provides
        @Singleton
        fun provideJson(stringResourceSerializers: StringResourceSerializers): Json = Json {
            serializersModule = SerializersModule {
                polymorphic(ValueUnit::class) {
                    subclass(MassUnit::class, PolymorphicEnumSerializer(MassUnit.serializer()))
                    subclass(
                        LongDistanceUnit::class,
                        PolymorphicEnumSerializer(LongDistanceUnit.serializer()),
                    )
                    subclass(
                        MediumDistanceUnit::class,
                        PolymorphicEnumSerializer(MediumDistanceUnit.serializer()),
                    )
                    subclass(
                        ShortDistanceUnit::class,
                        PolymorphicEnumSerializer(ShortDistanceUnit.serializer()),
                    )
                    subclass(PercentageUnit::class)
                }

                polymorphic(StringResource::class) {
                    stringResourceSerializers.forEach { (kClass, serializer) ->
                        subclass(kClass, serializer)
                    }
                }

                contextual(LocalDateSerializer())
            }
        }
    }
}
