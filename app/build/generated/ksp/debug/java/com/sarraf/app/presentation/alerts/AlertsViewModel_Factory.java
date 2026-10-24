package com.sarraf.app.presentation.alerts;

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
public final class AlertsViewModel_Factory implements Factory<AlertsViewModel> {
  private final Provider<CurrencyRepository> repositoryProvider;

  public AlertsViewModel_Factory(Provider<CurrencyRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AlertsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AlertsViewModel_Factory create(Provider<CurrencyRepository> repositoryProvider) {
    return new AlertsViewModel_Factory(repositoryProvider);
  }

  public static AlertsViewModel newInstance(CurrencyRepository repository) {
    return new AlertsViewModel(repository);
  }
}
