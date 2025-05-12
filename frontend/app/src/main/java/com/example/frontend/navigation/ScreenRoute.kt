package com.example.frontend.navigation


sealed class Screen(var route: String) {
    sealed class MainNav(route: String) :Screen(route) {
        data object Home : MainNav("Home")
        data object Search :MainNav("Search")
        data object YourStory : MainNav("YourStory")
        data object Community : MainNav("Community")
        data object Profile : MainNav("Profile")
    }
    sealed class Community(route: String) :Screen(route) {
        fun createRoute(communityId: String) = route.replace("{communityId}", communityId)
        data object Chat : Community("Chat/{communityId}")
        data object Detail :Community("CommunityDetail/{communityId}")
        data object  SearchingMember: Community("SearchingMember/{communityId}")
    }

    sealed class Story(route: String) :Screen(route) {
        data object Detail :Story("StoryDetail/{id}"){
            fun createRoute(id: String) = "StoryDetail/$id"
        }
        sealed class Chapter(route: String) :Story(route) {
            fun createRoute(chapterId: String) = route.replace("{chapterId}", chapterId)
            data object Read : Chapter("Read/{chapterId}")
            data object Write: Chapter("Write/{chapterId}")
        }
    }
    data object  YourStoryDetail: Screen("YourStoryDetail/{id}"){
        fun createRoute(id: String) = "StoryDetail/$id"
    }

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
    data object  StoryList: Screen("List/{id}"){
        fun createRoute(id: String) = "List/$id"
    }
}