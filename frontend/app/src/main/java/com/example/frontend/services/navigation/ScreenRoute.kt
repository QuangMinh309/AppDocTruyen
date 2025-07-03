package com.example.frontend.services.navigation

sealed class Screen(var route: String) {
    sealed class MainNav(route: String) : Screen(route) {
        data object Home : MainNav("Home")
        data object Search : MainNav("Search")
        data object YourStory : MainNav("YourStory")
        data object Community : MainNav("Community")
        data object Profile : MainNav("Profile")

//        data object Profile : MainNav("Profile/{id}") {
//            fun createRoute(id: String) = "Profile/$id"
//        }
    }

    data object Intro : Screen("Intro")
    data object Splash : Screen("Splash")
    sealed class Authentication(route: String) : Screen(route) {
        data object Login : Authentication("Authentication/Login")
        data object Register : Authentication("Authentication/Register")
        data object ResetPassword : Authentication("Authentication/ResetPassword")
        data object NewPassword : Authentication("Authentication/NewPassword/{otp}/{userId}") {
            fun createRoute(otp: String, userId: String) = "Authentication/NewPassword/$otp/$userId"
        }
        data object ChangePassword : Authentication("Authentication/ChangePassword")
    }

    sealed class Community(route: String) : Screen(route) {
        fun createRoute(communityId: String) = route.replace("{communityId}", communityId)
        data object Chat : Community("Community/Chat/{communityId}")
//        {
//            fun createRoute(communityId: String, name: String) = "Community/Chat/$communityId/$name"
//        }
        data object Detail : Community("Community/CommunityDetail/{communityId}")
        data object SearchingMember : Community("Community/SearchingMember/{communityId}")
    }

    sealed class Story(route: String) : Screen(route) {
        data object Detail : Story("Story/StoryDetail/{storyId}") {
            fun createRoute(storyId: Int) = "Story/StoryDetail/$storyId"
        }
        data object UserProfile : Story("Story/UserProfile/{userId}") {
            fun createRoute(userId: Int) = "Story/UserProfile/$userId"
        }
        sealed class Chapter(route: String) : Story(route) {

            data object Read : Chapter("Chapter/Read/{chapterId}/{finalChapterId}/{storyId}/{isAuthor}") {
                fun createRoute(chapterId: Int,finalChapterId:Int,storyId: Int,isAuthor:Boolean): String = "Chapter/Read/$chapterId/$finalChapterId/$storyId/$isAuthor"
            }

            data object Write : Chapter("Chapter/Write/{storyId}") {
                fun createRoute(storyId: Int): String = "Chapter/Write/$storyId"
            }

            data object Update : Chapter("Chapter/Update/{storyId}/{chapterId}") {
                fun createRoute(storyId: Int, chapterId:Int): String = "Chapter/Update/$storyId/$chapterId"
            }

        }
        data object TopRanking : Story("Story/TopRanking")
        data object Category : Story("Story/Category/{categoryId}/{categoryName}"){
            fun createRoute(categoryId:Int, categoryName:String)="Story/Category/$categoryId/$categoryName"
        }
        data object NameLists:Story("Story/NameLists/{nameListsId}"){
            fun createRoute(nameListsId:Int)="Story/NameLists/$nameListsId"
        }

        data object CreateStory :Story("Story/CreateStory")

        data object UpdateStory: Story("Story/UpdateStory/{storyId}"){
            fun createRoute(storyId: Int)="Story/UpdateStory/$storyId"
        }
        data object AddStoryToNameList:Story("Story/AddStoryToNameList/{storyId}"){
            fun createRoute(storyId: Int)="Story/AddStoryToNameList/$storyId"
        }

    }
// create story
    data object YourStoryDetail : Screen("YourStoryDetail/{id}") {
        fun createRoute(id: Int) = "YourStoryDetail/$id"
    }

    sealed class Transaction(route: String) : Screen(route) {
        data object Deposit : Transaction("Transaction/Deposit")
        data object Premium : Transaction("Transaction/Premium")
        data object Wallet : Transaction("Transaction/Wallet")
        data object WithDraw : Transaction("Transaction/WithDraw")
        data object Accept : Transaction("Accept/{type-of-transaction}/{depositMoney}") {
            fun createRoute(depositMoney: Long=0) = "Accept/${this.route.split("/")[2]}/$depositMoney"
        }
    }

    data object Report : Screen("Report/{userId}/{name}") {
        fun createRoute(userId: Int, name: String) = "Report/$userId/$name"
    }
    data object Notification : Screen("Notification")
    data object Setting : Screen("Setting")

    data object AdminScreen : Screen("Admin")
    sealed class Admin(route: String) : Screen(route) {
        data object Category : Admin("Admin/Category")
        data object Transaction : Admin("Admin/Transaction")
        data object User : Admin("Admin/UserMgmt")
        data object Story : Admin("Admin/Story")
        data object Community : Admin("Admin/Community")
        data object Revenue : Admin("Admin/Revenue")
        data object StoryDetail : Admin("Admin/StoryDetail/{id}") {
            fun createRoute(id: String) = "Admin/StoryDetail/$id"
        }
    }
}