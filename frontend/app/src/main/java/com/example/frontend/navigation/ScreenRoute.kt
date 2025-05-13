package com.example.frontend.navigation


sealed class Screen(val route: String) {
    sealed class MainNav(route: String) :Screen(route) {
        data object Home : MainNav("Home")
        data object Search :MainNav("Search")
        data object YourStory : MainNav("YourStory")
        data object Community : MainNav("Community")
        data object Profile : MainNav("Profile")
    }
    sealed class Community(route: String) :Screen(route) {
        data object Chat : Community("Chat")
        data object Detail :Community("CommunityDetail")
        data object  SearchingMember: Community("SearchingMember")
    }

    sealed class Story(route: String) :Screen(route) {
        data object Read : Story("Read")
        data object Detail :Story("StoryDetail")
        data object Write: Story("Write")
    }
    data object  YourStory: Screen("YourStoryDetail")

    sealed class Transaction(route: String) :Screen(route) {
        data object Deposit: Transaction("Deposit")
        data object Premium:Transaction("Premium")
        data object Wallet: Transaction("Wallet")
        data object WithDraw: Transaction("WithDraw")
        data object Accept: Transaction(" Accept")
    }

    data object  Discover: Screen("Discover")
    data object  Notification: Screen("Notification")
    data object  Setting: Screen("Setting")
    data object  StoryList: Screen("List")
}