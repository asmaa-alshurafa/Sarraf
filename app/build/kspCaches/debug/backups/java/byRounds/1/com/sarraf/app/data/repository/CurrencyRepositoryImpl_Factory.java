package com.sarraf.app.data.repository;

import com.sarraf.app.data.local.dao.ExchangeRateDao;
import com.sarraf.app.data.preferences.UserPreferencesManager;
import com.sarraf.app.data.remote.ExchangeRateApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

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
public final class CurrencyRepositoryImpl_Factory implements Factory<CurrencyRepositoryImpl> {
  private final Provider<ExchangeRateApiService> apiServiceProvider;

  private final Provider<ExchangeRateDao> daoProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public CurrencyRepositoryImpl_Factory(Provider<ExchangeRateApiService> apiServiceProvider,
      Provider<ExchangeRateDao> daoProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.daoProvider = daoProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public CurrencyRepositoryImpl get() {
    return newInstance(apiServiceProvider.get(), daoProvider.get(), preferencesManagerProvider.get(), ioDispatcherProvider.get());
  }

  public static CurrencyRepositoryImpl_Factory create(
      Provider<ExchangeRateApiService> apiServiceProvider, Provider<ExchangeRateDao> daoProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new CurrencyRepositoryImpl_Factory(apiServiceProvider, daoProvider, preferencesManagerProvider, ioDispatcherProvider);
  }

  public static CurrencyRepositoryImpl newInstance(ExchangeRateApiService apiService,
      ExchangeRateDao dao, UserPreferencesManager preferencesManager,
      CoroutineDispatcher ioDispatcher) {
    return new CurrencyRepositoryImpl(apiService, dao, preferencesManager, ioDispatcher);
  }
}
