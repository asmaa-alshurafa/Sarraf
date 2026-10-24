package com.sarraf.app.domain.usecase

import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    operator fun invoke(baseCurrency: String): Flow<List<CurrencyRate>> {
        return repository.getRatesFlow(baseCurrency)
    }

    suspend fun refresh(baseCurrency: String): Result<Unit> {
        return repository.refreshRates(baseCurrency)
    }
}
