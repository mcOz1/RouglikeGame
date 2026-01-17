# ⚔️ RouglikeGame - Shadow Tap: Rogue Waves

## 1. Opis projektu
**RouglikeGame** to mobilna gra typu "clicker roguelike" stworzona na platformę Android. Gracz wciela się w bohatera, który musi odpierać kolejne fale przeciwników, ulepszać swoje statystyki i dążyć do osiągnięcia jak najwyższego wyniku.

## 2. Stos technologiczny
*   **Język:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Architektura:** MVVM (Model-View-ViewModel)
*   **Zarządzanie stanem:** Kotlin Coroutines & StateFlow
*   **Obrazy:** Coil (ładowanie assetów)
*   **Budowanie:** Gradle (Kotlin DSL)

## 3. Struktura Projektu
*   `model/`: Klasy danych (`GameState`, `Enemy`, `PlayerStats`).
*   `viewmodel/`: Logika biznesowa (`GameViewModel`).
*   `ui/screens/`: Ekrany gry (`Menu`, `Play`, `LevelUp`, `GameOver`).
*   `ui/components/`: Współdzielone komponenty i definicje wizualne (`GameColors`).

## 4. Mechanika Rozgrywki
Gra wykorzystuje maszynę stanów (`GameState`):
1.  **MENU:** Ekran startowy z animacjami.
2.  **PLAYING:** Walka z przeciwnikami poprzez interakcję (tap).
3.  **LEVEL_UP:** Wybór ulepszeń (np. zwiększenie siły ataku, regeneracja zdrowia).
4.  **PAUSE:** Wstrzymanie gry.
5.  **GAME_OVER:** Ekran końcowy z podsumowaniem statystyk.
---
*Dokumentacja techniczna v1.0*
