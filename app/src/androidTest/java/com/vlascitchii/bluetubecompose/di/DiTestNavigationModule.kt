package com.vlascitchii.bluetubecompose.di

import androidx.paging.PagingData
import com.vlascitchii.bluetubecompose.fake.converter.FakePlayerConverter
import com.vlascitchii.bluetubecompose.fake.converter.FakeShortsConverter
import com.vlascitchii.bluetubecompose.fake.converter.FakeVideoListConverter
import com.vlascitchii.bluetubecompose.fake.network_connectivity.FakeNetworkConnectivity
import com.vlascitchii.bluetubecompose.fake.usecase.FakeShortsUseCase
import com.vlascitchii.bluetubecompose.fake.usecase.FakeVideoListTestUseCase
import com.vlascitchii.bluetubecompose.fake.usecase.FakeVideoPlayerUseCase
import com.vlascitchii.domain.di_common.BindUseCaseDiModule
import com.vlascitchii.domain.di_common.PLAYER_CONVERTER
import com.vlascitchii.domain.di_common.SHORTS_CONVERTER
import com.vlascitchii.domain.di_common.SHORTS_USE_CASE
import com.vlascitchii.domain.di_common.VIDEO_LIST_CONVERTER
import com.vlascitchii.domain.di_common.VIDEO_LIST_USE_CASE
import com.vlascitchii.domain.di_common.VIDEO_PLAYER_USE_CASE
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.presentation_common.di.CommonDiModule
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BindUseCaseDiModule::class]
)
abstract class DiTestNavigationUseCaseModule {

    @Binds
    @Named(VIDEO_LIST_USE_CASE)
    abstract fun bindFakeVideoListUseCase(fakeVideoListTestUseCase: FakeVideoListTestUseCase)
    : UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>

    @Binds
    @Named(SHORTS_USE_CASE)
    abstract fun bindFakeShortsUseCase(fakeShortsUseCase: FakeShortsUseCase)
    : UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse>

    @Binds
    @Named(VIDEO_PLAYER_USE_CASE)
    abstract fun bindFakeVideoPlayerUseCase(fakeVideoPlayerUseCase: FakeVideoPlayerUseCase)
    : UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse>
}


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConverterDiModule::class]
)
abstract class DiTestNavigationConverterModule {

    @Binds
    @Named(VIDEO_LIST_CONVERTER)
    abstract fun bindFakeVideoListConverter(fakeVideoListConverter: FakeVideoListConverter)
            : CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

    @Binds
    @Named(PLAYER_CONVERTER)
    abstract fun bindFakePlayerConverter(fakePlayerConverter: FakePlayerConverter)
            : CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>

    @Binds
    @Named(SHORTS_CONVERTER)
    abstract fun bindFakeShortsConverter(fakeShortsConverter: FakeShortsConverter)
            : CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>
}


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CommonDiModule::class]
)
abstract class DiTestNavigationConnectivityModule {

    @Binds
    @Singleton
    abstract fun provideConnectivityObserver(fakeNetworkConnectivity: FakeNetworkConnectivity): NetworkConnectivityAbstraction
}
