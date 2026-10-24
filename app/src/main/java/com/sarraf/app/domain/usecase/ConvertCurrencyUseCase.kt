package com.sarraf.app.domain.usecase

import com.sarraf.app.domain.model.ConversionResult
import com.sarraf.app.domain.repository.CurrencyRepository
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(
        fromCode: String,
        toCode: String,
        amount: Double
    ): Result<ConversionResult> {
        return repository.convertCurrency(fromCode, toCode, amount)
    }
}
