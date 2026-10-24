package com.sarraf.app.di;

import com.sarraf.app.data.remote.ExchangeRateApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NetworkModule_ProvideExchangeRateApiServiceFactory implements Factory<ExchangeRateApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideExchangeRateApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ExchangeRateApiService get() {
    return provideExchangeRateApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideExchangeRateApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideExchangeRateApiServiceFactory(retrofitProvider);
  }

  public static ExchangeRateApiService provideExchangeRateApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideExchangeRateApiService(retrofit));
  }
}
