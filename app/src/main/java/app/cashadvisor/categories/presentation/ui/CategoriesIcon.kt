package app.cashadvisor.categories.presentation.ui

import androidx.annotation.DrawableRes
import app.cashadvisor.R

enum class CategoriesIcon(
    @DrawableRes val imageResId: Int,
    val imageId: Int) {

    WRAPPED_GIFT(app.cashadvisor.uikit.R.drawable.ic_wrapped_gift, 1),
    SAVINGS(app.cashadvisor.uikit.R.drawable.ic_savings, 2),
    EMPLOYER(app.cashadvisor.uikit.R.drawable.ic_employer, 3),
    WHALE(app.cashadvisor.uikit.R.drawable.ic_whale, 4),
    BOOKS(app.cashadvisor.uikit.R.drawable.ic_books, 5),
    GUITAR(app.cashadvisor.uikit.R.drawable.ic_guitar, 6),
    PARTY_POPPER(app.cashadvisor.uikit.R.drawable.ic_party_popper,7),
    BASKETBALL(app.cashadvisor.uikit.R.drawable.ic_basketball,8),
    CAMPING(app.cashadvisor.uikit.R.drawable.ic_camping, 9),
    BEACH_UMBRELLA(app.cashadvisor.uikit.R.drawable.ic_beach_umbrella, 10),
    GRAD_CAP(app.cashadvisor.uikit.R.drawable.ic_grad_cap, 11),
    AUTOMOBILE(app.cashadvisor.uikit.R.drawable.ic_automobile, 12),
    CALENDAR(app.cashadvisor.uikit.R.drawable.ic_calendar,13),
    HANDSHAKE(app.cashadvisor.uikit.R.drawable.ic_handshake, 14),
    MOBILE_PHONE(app.cashadvisor.uikit.R.drawable.ic_mobile_phone, 15),
    OFFICE_BUILDING(app.cashadvisor.uikit.R.drawable.ic_mobile_phone, 16),
    LAPTOP(app.cashadvisor.uikit.R.drawable.ic_laptop, 17),
    PINATA(app.cashadvisor.uikit.R.drawable.ic_pinata, 18),
    FLYING_SAUCER(app.cashadvisor.uikit.R.drawable.ic_flying_saucer, 19),
    ARTIST_PALETTE(app.cashadvisor.uikit.R.drawable.ic_artist_palette,20),
    HOUSE(app.cashadvisor.uikit.R.drawable.ic_house, 21),
    PIZZA(app.cashadvisor.uikit.R.drawable.ic_pizza, 22),
    HAMMER_AND_WRENCH(app.cashadvisor.uikit.R.drawable.ic_hammer_and_wrench, 23),
    CURRENCY(app.cashadvisor.uikit.R.drawable.ic_currency, 24),
    BRIFCASE(app.cashadvisor.uikit.R.drawable.ic_briefcase, 25),
}