package com.vlascitchii.data_remote.enetity_api_model.video_search_api_model

import com.squareup.moshi.JsonClass
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailAttributesApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.ThumbnailsApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.VideoSnippetApiModel

@JsonClass(generateAdapter = true)
data class SearchVideoResponseApiModel (
    val nextPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<SearchVideoItemApiModel> = emptyList()
)

private val RESPONSE_SEARCH_VIDEO_LIST = listOf(
    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "bbM6aSB6iMQ"),
        snippet = VideoSnippetApiModel(
            "State of Origin 2024 | Blues v Maroons | Match Highlights",
            "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
            "2024-06-05T12:22:55Z",
            "NRL - National Rugby League",
            "",
            channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "990CmzkhEq4"),
        snippet = VideoSnippetApiModel(
            "I Tried The Best Taco In The World",
            "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
            "2024-06-09T14:30:03Z",
            "Joshua Weissman",
            "",
            channelId = "UChBEbMKI1eCcejTtmI32UEw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "D7UqN8_kTpg"),
        snippet = VideoSnippetApiModel(
            "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
            "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
            "2024-06-09T00:51:03Z",
            "ErikTheElectric",
            "",
            channelId = "UC6huXz0F6-7KA7-mW0jdejA",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "acxSH1A536Q"),
        snippet = VideoSnippetApiModel(
            "Hombre al Agua - RIP ANKHAL",
            "Hombre al agua... #RIPANKHAL #Tiraera",
            "2024-06-08T23:41:27Z",
            "Noriel",
            "",
            channelId = "UCLDAM_6WwevNIPHz7IalMLw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "Q9rlqktHiF0"),
        snippet = VideoSnippetApiModel(
            "I'm Running Out Of Time..",
            "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
            "2024-06-08T17:00:16Z",
            "DanTDM",
            "",
            channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    )
)

val DEFAULT_SEARCH_VIDEO_RESPONSE = SearchVideoResponseApiModel(
    nextPageToken = "CAoQAA",
    prevPageToken = "CAUQAQ",
    RESPONSE_SEARCH_VIDEO_LIST
)


private val RESPONSE_SEARCH_VIDEO_LIST_WITH_CHANNEL_IMG_URL = listOf(
    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "bbM6aSB6iMQ"),
        snippet = VideoSnippetApiModel(
            "State of Origin 2024 | Blues v Maroons | Match Highlights",
            "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
            "2024-06-05T12:22:55Z",
            "NRL - National Rugby League",
            "https://yt3.ggpht.com/Pui0xFjDp_F-x0UK5Rkfh1tGkwZLy7fkU4Pr44Zht3DJcMwWXLnxoVIGSRS5Pr7SVxgES0-ouA=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "990CmzkhEq4"),
        snippet = VideoSnippetApiModel(
            "I Tried The Best Taco In The World",
            "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
            "2024-06-09T14:30:03Z",
            "Joshua Weissman",
            "https://yt3.ggpht.com/ytc/AIdro_nfXRvoxu5cFt2H4WhJfFLbL5SVdzmvEnFymnPzH3_1qPM=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UChBEbMKI1eCcejTtmI32UEw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "D7UqN8_kTpg"),
        snippet = VideoSnippetApiModel(
            "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
            "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
            "2024-06-09T00:51:03Z",
            "ErikTheElectric",
            "https://yt3.ggpht.com/ytc/AIdro_nDTKq9EfTx0SA4Tv4FV9MfCh7zLXl0p4m2CjBwVyVybs4=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC6huXz0F6-7KA7-mW0jdejA",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "acxSH1A536Q"),
        snippet = VideoSnippetApiModel(
            "Hombre al Agua - RIP ANKHAL",
            "Hombre al agua... #RIPANKHAL #Tiraera",
            "2024-06-08T23:41:27Z",
            "Noriel",
            "https://yt3.ggpht.com/MamDoeUwqyGIowFfBJ4teXOuHSuwpUdSwv_EcanSB99-VIKxExaoEpm80qwEo5uiaGJCYXuGiw=s240-c-k-c0x00ffffff-no-nd-rj",
            channelId = "UCLDAM_6WwevNIPHz7IalMLw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItemApiModel(
        SearchVideoItemIdApiModel(videoId = "Q9rlqktHiF0"),
        snippet = VideoSnippetApiModel(
            "I'm Running Out Of Time..",
            "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
            "2024-06-08T17:00:16Z",
            "DanTDM",
            "https://yt3.ggpht.com/_SQKtbdn1Xv6vkV_OX5D1jTNQY2SAQ5Gq-WSPlTbc8h5qMHQ3dnwpSXlpq8ADikltH3zC9KC5A=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
            thumbnails = ThumbnailsApiModel(
                ThumbnailAttributesApiModel(
                    "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    )
)

val DEFAULT_SEARCH_VIDEO_RESPONSE_WITH_CHANNEL_IMG_URL = SearchVideoResponseApiModel(
    nextPageToken = "CAoQAA",
    prevPageToken = "CAUQAQ",
    RESPONSE_SEARCH_VIDEO_LIST_WITH_CHANNEL_IMG_URL
)
