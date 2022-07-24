Decided to go with MVVM. Following were components used:
1. Android Viewmodels
2. Room database
3. Coroutines for concurrency
4. Kotlin Flows
5. Dagger for dependancy injection

Tests added: 
1. Unit tests for Viewmodels, Repositories
2. Instrumented test for db operations sanity check
3. Integration test to check if network stack works fine.


Following a code organisation structure based on layers.

API -> Repository -> Viewmodel ---Android--> Activity (Composables)

Choices:
1. Multi activity structure: In the interest of time, using two activities. If I had more time I would investigate compose navigation and use that.
2. State saving using onSavedInstanceState() : In the interest of time, decided to use simple activity based solution to manage config changes. Given more time, would implement it using SavedStateHandle and SavedStateRegistry.