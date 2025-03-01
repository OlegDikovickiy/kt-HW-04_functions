

const val ERROR_TYPE_CARD = "Ошибка: неизвестный тип карты"
const val ERROR_SINGLE_LIMIT_VK = "Ошибка: превышен лимит перевода VK Pay (15 000 руб. за раз)"
const val ERROR_MONTH_LIMIT_VK = "Ошибка: превышен месячный лимит VK Pay (40 000 руб.)"
const val ERROR_DAILY_LIMIT = "Ошибка: превышен суточный лимит в 150 000 руб."
const val ERROR_MONTH_LIMIT = "Ошибка: превышен месячный лимит в 600 000 руб."



fun main() {
    println(transfer("UnionPay", 500, 0, 1000))  // Неизвестная карта
//    println(transfer("Maestro", 290, 0, 10000))  // Комиссия 0.6% + 20 руб., т.к. менее 300 руб.
//    println(transfer("Mastercard", 5000, 74000, 20000))  // В пределах акции, комиссия 0
//    println(transfer("Visa", 0, 10000, 50000))  // Комиссия 0.75%, но не менее 35 руб.
//    println(transfer("Мир", 0, 0, 20000))  // Комиссия 0.75%
//    println(transfer("VK Pay", 0, 0, 5000))  // Без комиссии
//    println(transfer("VK Pay", 0, 30000, 20000))  // Ошибка: превышен месячный лимит VK Pay
}

fun transfer(cardType: String, previousTransfers: Int, monthlySent: Int, amount: Int): String {
    // Лимиты для карт
    val dailyLimit = 150_000
    val monthlyLimit = 600_000

    // Лимиты для VK Pay
    val vkPaySingleLimit = 15_000
    val vkPayMonthlyLimit = 40_000

    // Проверка лимитов
    if (cardType == "VK Pay") {
        if (amount > vkPaySingleLimit) return ERROR_SINGLE_LIMIT_VK
        if (monthlySent + amount > vkPayMonthlyLimit) return ERROR_MONTH_LIMIT_VK
        return "Перевод: $amount руб. Комиссия: 0 руб."
    } else {
        if (amount > dailyLimit) return ERROR_DAILY_LIMIT
        if (monthlySent + amount > monthlyLimit) return ERROR_MONTH_LIMIT
    }

    // Рассчитываем комиссию
    val commission = when (cardType) {
        "Mastercard", "Maestro" -> {
            if (amount < 300 || previousTransfers + amount > 75_000)
                (amount * 0.006 + 20).toInt()
            else 0
        }

        "Visa", "Мир" -> (amount * 0.0075).coerceAtLeast(35.0).toInt()
        else -> return ERROR_TYPE_CARD
    }

    return "Перевод: $amount руб. Комиссия: $commission руб."
}