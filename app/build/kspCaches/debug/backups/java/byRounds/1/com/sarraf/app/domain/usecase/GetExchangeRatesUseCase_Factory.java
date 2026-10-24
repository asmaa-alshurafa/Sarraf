package com.sarraf.app.domain.usecase;

import com.sarraf.app.domain.repository.CurrencyRepository;
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
public final class GetExchangeRatesUseCase_Factory implements Factory<GetExchangeRatesUseCase> {
  private final Provider<CurrencyRepository> repositoryProvider;

  public GetExchangeRatesUseCase_Factory(Provider<CurrencyRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetExchangeRatesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetExchangeRatesUseCase_Factory create(
      Provider<CurrencyRepository> repositoryProvider) {
    return new GetExchangeRatesUseCase_Factory(repositoryProvider);
  }

  public static GetExchangeRatesUseCase newInstance(CurrencyRepository repository) {
    return new GetExchangeRatesUseCase(repository);
  }
}
