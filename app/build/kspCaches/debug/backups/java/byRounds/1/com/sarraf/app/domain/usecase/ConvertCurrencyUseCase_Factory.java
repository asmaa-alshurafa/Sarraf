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
public final class ConvertCurrencyUseCase_Factory implements Factory<ConvertCurrencyUseCase> {
  private final Provider<CurrencyRepository> repositoryProvider;

  public ConvertCurrencyUseCase_Factory(Provider<CurrencyRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ConvertCurrencyUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ConvertCurrencyUseCase_Factory create(
      Provider<CurrencyRepository> repositoryProvider) {
    return new ConvertCurrencyUseCase_Factory(repositoryProvider);
  }

  public static ConvertCurrencyUseCase newInstance(CurrencyRepository repository) {
    return new ConvertCurrencyUseCase(repository);
  }
}
