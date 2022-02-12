package com.lucreziacarena.mycoachassistant.repository.api



class APIUrls {
    companion object{
        const val BASE_URL = "https://randomuser.me/"

 //"api/?seed=empatica&inc=name,picture&gender=male&results=10&noinfo"
        const val ATHLETICS_LIST = "api/{seed}{inc}{gender}{results}&noinfo"
    }
}
