package com.sarraf.app.presentation.settings;

import com.sarraf.app.data.preferences.UserPreferencesManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  private final Provider<CurrencyRepository> currencyRepositoryProvider;

  public SettingsViewModel_Factory(Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<CurrencyRepository> currencyRepositoryProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.currencyRepositoryProvider = currencyRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get(), currencyRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<UserPreferencesManager> preferencesManagerProvider,
      Provider<CurrencyRepository> currencyRepositoryProvider) {
    return new SettingsViewModel_Factory(preferencesManagerProvider, currencyRepositoryProvider);
  }

  public static SettingsViewModel newInstance(UserPreferencesManager preferencesManager,
      CurrencyRepository currencyRepository) {
    return new SettingsViewModel(preferencesManager, currencyRepository);
  }
}
