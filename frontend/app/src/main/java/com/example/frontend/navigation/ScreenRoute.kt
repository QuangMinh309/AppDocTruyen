package com.example.frontend.navigation


sealed class Screen(var route: String) {
    sealed class MainNav(route: String) :Screen(route) {
        data object Home : MainNav("Home")
        data object Search :MainNav("Search")
        data object YourStory : MainNav("YourStory")
        data object Community : MainNav("Community")
        data object Profile : MainNav("Profile/{id}"){
            fun createRoute(id: String) = "Profile/$id"
        }
    }

    data object Intro: Screen("Intro")
    sealed class  Authentication(route: String) :Screen(route) {
        data object Login : Authentication("Authentication/Login")
        data object Register : Authentication("Authentication/Register")
        data object ResetPassword : Authentication("Authentication/ResetPassword")
        data object NewPassword : Authentication("Authentication/NewPassword/{id}"){
            fun createRoute(id: String) = "Authentication/NewPassword/$id"
        }
    }
    sealed class Community(route: String) :Screen(route) {
        fun createRoute(communityId: String) = route.replace("{communityId}", communityId)
        data object Chat : Community("Community/Chat/{communityId}")
        data object Detail :Community("Community/CommunityDetail/{communityId}")
        data object  SearchingMember: Community("Community/SearchingMember/{communityId}")
    }

    sealed class Story(route: String) :Screen(route) {
        data object Detail :Story("Story/StoryDetail/{id}"){
            fun createRoute(id: String) = "Story/StoryDetail/$id"
        }
        data object AuthorProfile : Story("AuthorProfile/{id}"){
            fun createRoute(id: String) = "AuthorProfile/$id"
        }
        sealed class Chapter(route: String) :Story(route) {
            fun createRoute(chapterId: String) = route.replace("{chapterId}", chapterId)
            data object Read : Chapter("Chapter/Read/{chapterId}")
            data object Write: Chapter("chapter/Write/{chapterId}")
        }
    }
    data object  YourStoryDetail: Screen("YourStoryDetail/{id}"){
        fun createRoute(id: String) = "YourStoryDetail/$id"
    }

    sealed class Transaction(route: String) :Screen(route) {
        data object Deposit: Transaction("Transaction/Deposit")
        data object Premium:Transaction("Transaction/Premium")
        data object Wallet: Transaction("Transaction/Wallet")
        data object WithDraw: Transaction("Transaction/WithDraw")
        data object Accept: Transaction(" Accept/{depositMoney}"){
            fun createRoute(depositMoney:Long)="Accept/$depositMoney"
        }
    }


    data object  Discover: Screen("Discover")
    data object  Notification: Screen("Notification")
    data object  Setting: Screen("Setting")
    data object  StoryList: Screen("List/{id}"){
        fun createRoute(id: String) = "List/$id"
    }
}
