package com.dicoding.submissionone.data

import com.google.gson.annotations.SerializedName

data class UserListResponse(

	@field:SerializedName("items")
	val userListResponse: List<UserResponse>,
)

data class UserResponse(

	@field:SerializedName("login")
	val login: String = "",

	@field:SerializedName("avatar_url")
	val avatarUrl: String = "",

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("hireable")
	val hireable: Any = "",

	@field:SerializedName("bio")
	val bio: Any = "",

	@field:SerializedName("blog")
	val blog: String = "",

	@field:SerializedName("public_gists")
	val publicGists: Int = 0,

	@field:SerializedName("followers")
	val followers: Int = 0,

	@field:SerializedName("following")
	val following: Int = 0,

	@field:SerializedName("company")
	val company: String = "",

	@field:SerializedName("location")
	val location: String = "",

	@field:SerializedName("public_repos")
	val publicRepos: Int = 0,

	@field:SerializedName("email")
	val email: Any = ""
)
