1. Opis projektu
RouglikeGame (tytuł roboczy: Shadow Tap: Rogue Waves) to mobilna gra typu "clicker roguelike" stworzona na platformę Android. Gracz wciela się w bohatera, który musi odpierać fale przeciwników, ulepszać swoje statystyki i dążyć do osiągnięcia jak najwyższego wyniku (liczba pokonanych wrogów).
2. Stos technologiczny
•
Język: Kotlin
•
UI Framework: Jetpack Compose (Material 3)
•
Architektura: MVVM (Model-View-ViewModel)
•
Zarządzanie stanem: Kotlin Coroutines & StateFlow
•
Obrazy: Coil (do ładowania assetów, np. goblin.png)
•
Budowanie: Gradle (Kotlin DSL)
3. Struktura Projektu (Główne pakiety)
•
com.example.rouglikegame.model: Zawiera klasy danych, takie jak GameState, definicje przeciwników i statystyki gracza.
•
com.example.rouglikegame.viewmodel: Logika biznesowa gry (GameViewModel), zarządzanie pętlą gry, obliczanie obrażeń i zmiana stanów.
•
com.example.rouglikegame.ui.screens: Definicje ekranów Compose (MainScreen, MenuScreen, GameOverScreen itp.).
•
com.example.rouglikegame.ui.components: Współdzielone komponenty UI i definicje kolorów (GameColors).
4. Logika Gry (Game States)
Gra opiera się na maszynie stanów zdefiniowanej w GameState:
1.
MENU: Ekran startowy z animacją pulsującego tła.
2.
PLAYING: Główna rozgrywka (walka z przeciwnikami poprzez tapnięcia).
3.
LEVEL_UP: Ekran wyboru ulepszeń po pokonaniu odpowiedniej liczby wrogów.
4.
PAUSE: Wstrzymanie rozgrywki.
5.
GAME_OVER: Podsumowanie wyniku i opcja restartu.
5. Kluczowe Funkcje UI
•
Animacje: Wykorzystanie rememberInfiniteTransition do tworzenia efektów wizualnych w menu (pulsowanie przycisku i tła).
•
Stylizacja: Spójna paleta barw zdefiniowana w GameColors (ciemne tła, neonowe akcenty, gradienty).
•
Responsywność: Layouty oparte na Box i Column, dostosowujące się do wielkości ekranu.
6. Jak uruchomić projekt
1.
Otwórz projekt w Android Studio (Ladybug lub nowsze).
2.
Zsynchronizuj pliki Gradle.
3.
Uruchom aplikację na emulatorze lub fizycznym urządzeniu z Androidem (min SDK: 24, Target SDK: 35).
