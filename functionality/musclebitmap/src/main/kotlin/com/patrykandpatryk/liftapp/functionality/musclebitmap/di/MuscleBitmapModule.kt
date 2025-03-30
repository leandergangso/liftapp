package com.patrykandpatryk.liftapp.functionality.musclebitmap.di

import android.content.Context
import com.patrykandpatryk.liftapp.core.R
import com.patrykandpatryk.liftapp.domain.Constants.Algorithms.SHA1_NAME
import com.patrykandpatryk.liftapp.domain.muscle.MuscleImageProvider
import com.patrykandpatryk.liftapp.functionality.musclebitmap.MuscleBitmapConfig
import com.patrykandpatryk.liftapp.functionality.musclebitmap.MuscleBitmapGenerator
import com.patrykandpatryk.liftapp.functionality.musclebitmap.MuscleBitmapGeneratorImpl
import com.patrykandpatryk.liftapp.functionality.musclebitmap.model.NameInfoCoder
import com.patrykandpatryk.liftapp.functionality.musclebitmap.model.NameInfoEncoder
import com.patrykandpatryk.liftapp.functionality.musclebitmap.provider.MuscleImageProviderImpl
import com.patrykandpatryk.liftapp.functionality.musclebitmap.provider.ResourceBitmapProvider
import com.patrykandpatryk.liftapp.functionality.musclebitmap.provider.ResourceBitmapProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.MessageDigest

@Module
@InstallIn(SingletonComponent::class)
interface MuscleBitmapModule {

    @Binds
    fun bindResourceBitmapProvider(provider: ResourceBitmapProviderImpl): ResourceBitmapProvider

    @Binds
    fun bindMuscleBitmapGenerator(generator: MuscleBitmapGeneratorImpl): MuscleBitmapGenerator

    @Binds fun bindMuscleImageProvider(provider: MuscleImageProviderImpl): MuscleImageProvider

    @Binds fun bindNameInfoEncoder(coder: NameInfoCoder): NameInfoEncoder

    companion object {

        @Provides
        fun provideBitmapConfig(context: Context): MuscleBitmapConfig =
            MuscleBitmapConfig(
                borderColor = context.getColor(R.color.muscle_border),
                primaryColor = context.getColor(R.color.muscle_primary),
                secondaryColor = context.getColor(R.color.muscle_secondary),
                tertiaryColor = context.getColor(R.color.muscle_tertiary),
                bitmapMargin = context.resources.getDimensionPixelSize(R.dimen.bitmap_margin),
            )

        @Provides fun provideSha1Algorithm(): MessageDigest = MessageDigest.getInstance(SHA1_NAME)
    }
}
