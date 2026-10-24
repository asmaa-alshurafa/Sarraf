package com.sarraf.app.presentation.rates;

import com.sarraf.app.data.preferences.UserPreferencesManager;
import com.sarraf.app.domain.usecase.GetExchangeRatesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class RatesViewModel_Factory implements Factory<RatesViewModel> {
  private final Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public RatesViewModel_Factory(Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.getExchangeRatesUseCaseProvider = getExchangeRatesUseCaseProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public RatesViewModel get() {
    return newInstance(getExchangeRatesUseCaseProvider.get(), preferencesManagerProvider.get());
  }

  public static RatesViewModel_Factory create(
      Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new RatesViewModel_Factory(getExchangeRatesUseCaseProvider, preferencesManagerProvider);
  }

  public static RatesViewModel newInstance(GetExchangeRatesUseCase getExchangeRatesUseCase,
      UserPreferencesManager preferencesManager) {
    return new RatesViewModel(getExchangeRatesUseCase, preferencesManager);
  }
}
