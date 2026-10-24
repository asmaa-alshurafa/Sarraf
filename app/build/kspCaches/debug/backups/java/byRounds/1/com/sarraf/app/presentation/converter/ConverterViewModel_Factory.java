package com.sarraf.app.presentation.converter;

import androidx.lifecycle.SavedStateHandle;
import com.sarraf.app.data.preferences.UserPreferencesManager;
import com.sarraf.app.domain.usecase.ConvertCurrencyUseCase;
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
public final class ConverterViewModel_Factory implements Factory<ConverterViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ConvertCurrencyUseCase> convertCurrencyUseCaseProvider;

  private final Provider<UserPreferencesManager> preferencesManagerProvider;

  public ConverterViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ConvertCurrencyUseCase> convertCurrencyUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.convertCurrencyUseCaseProvider = convertCurrencyUseCaseProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public ConverterViewModel get() {
    return newInstance(savedStateHandleProvider.get(), convertCurrencyUseCaseProvider.get(), preferencesManagerProvider.get());
  }

  public static ConverterViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ConvertCurrencyUseCase> convertCurrencyUseCaseProvider,
      Provider<UserPreferencesManager> preferencesManagerProvider) {
    return new ConverterViewModel_Factory(savedStateHandleProvider, convertCurrencyUseCaseProvider, preferencesManagerProvider);
  }

  public static ConverterViewModel newInstance(SavedStateHandle savedStateHandle,
      ConvertCurrencyUseCase convertCurrencyUseCase, UserPreferencesManager preferencesManager) {
    return new ConverterViewModel(savedStateHandle, convertCurrencyUseCase, preferencesManager);
  }
}
