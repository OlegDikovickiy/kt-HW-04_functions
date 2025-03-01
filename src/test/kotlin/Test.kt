import junit.framework.TestCase.assertEquals
import org.junit.Test

class Test {

    @Test // Неизвестная карта
    fun unknownCardType() {
        val cardType = "UnionPay"
        val previousTransfers = 500
        val monthlySent = 0
        val amount = 1_000

        val result = transfer(cardType, previousTransfers, monthlySent, amount)
        assertEquals(ERROR_TYPE_CARD, result)
    }

    @Test //В пределах акции для Maestro, комиссия 0
    fun testMaestro() {
        val cardType = "Maestro"
        val previousTransfers = 290
        val monthlySent = 0
        val amount = 10_000

        val result = transfer(cardType, previousTransfers, monthlySent, amount)
        assertEquals("Перевод: 10000 руб. Комиссия: 0 руб.", result)
    }

    @Test //В пределах акции для Mastercard, комиссия 0
    fun testActionMastercard() {
        assertEquals("Перевод: 20000 руб. Комиссия: 0 руб.", transfer("Mastercard", 5000, 74000, 20000))
    }

    @Test //Комиссия за пределлами акции для Mastercard
    fun testMaxOutsideActionMastercard() {
        assertEquals("Перевод: 100000 руб. Комиссия: 620 руб.", transfer("Maestro", 5000, 8000, 100000))
    }

    @Test //Комиссия за пределлами акции для Maestro
    fun testMaxOutsideActionMaestro() {
        assertEquals("Перевод: 100000 руб. Комиссия: 620 руб.", transfer("Maestro", 5000, 8000, 100000))
    }

    @Test //Комиссия за пределлами акции для Mastercard
    fun testMinOutsideActionMastercard() {
        assertEquals("Перевод: 200 руб. Комиссия: 21 руб.", transfer("Mastercard", 5000, 8000, 200))
    }

    @Test //Комиссия за пределлами акции для Maestro
    fun testMinOutsideActionMaestro() {
        assertEquals("Перевод: 200 руб. Комиссия: 21 руб.", transfer("Maestro", 5000, 8000, 200))
    }

    @Test //Комиссия 0.75% Visa
    fun testVisaPercent() {
        assertEquals("Перевод: 50000 руб. Комиссия: 375 руб.", transfer("Visa", 0, 10000, 50000))
    }

    @Test //Комиссия не мене 35 рублей. Visa
    fun testMirMinCommission() {
        assertEquals("Перевод: 1000 руб. Комиссия: 35 руб.", transfer("Мир", 0, 10000, 1000))
    }

    @Test //Без комиссии VK Pay
    fun testVKPayFree() {
        assertEquals("Перевод: 5000 руб. Комиссия: 0 руб.", transfer("VK Pay", 0, 0, 5000))
    }

    @Test //VK Pay больше допустимого лимита за раз
    fun testVKPaySingleLimit() {
        assertEquals(ERROR_SINGLE_LIMIT_VK, transfer("VK Pay", 0, 0, 50000))
    }

    @Test //VK Pay больше допустимого лимита за месяц
    fun testVKPayMonthLimit() {
        assertEquals(ERROR_MONTH_LIMIT_VK, transfer("VK Pay", 0, 35000, 10000))
    }

    @Test //Больше допустимого дневного лимита
    fun testCardOutsideDailyLimit() {
        assertEquals(ERROR_DAILY_LIMIT, transfer("Mastercard", 5000, 60000, 200000))
    }

    @Test //Больше допустимого месячного лимита
    fun testCardOutsideMonthLimit() {
        assertEquals(ERROR_MONTH_LIMIT, transfer("Maestro", 0, 599000, 5000))
    }


}