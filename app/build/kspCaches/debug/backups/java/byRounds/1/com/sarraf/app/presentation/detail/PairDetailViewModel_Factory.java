package com.sarraf.app.presentation.detail;

import androidx.lifecycle.SavedStateHandle;
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
public final class PairDetailViewModel_Factory implements Factory<PairDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider;

  public PairDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getExchangeRatesUseCaseProvider = getExchangeRatesUseCaseProvider;
  }

  @Override
  public PairDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getExchangeRatesUseCaseProvider.get());
  }

  public static PairDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetExchangeRatesUseCase> getExchangeRatesUseCaseProvider) {
    return new PairDetailViewModel_Factory(savedStateHandleProvider, getExchangeRatesUseCaseProvider);
  }

  public static PairDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetExchangeRatesUseCase getExchangeRatesUseCase) {
    return new PairDetailViewModel(savedStateHandle, getExchangeRatesUseCase);
  }
}
