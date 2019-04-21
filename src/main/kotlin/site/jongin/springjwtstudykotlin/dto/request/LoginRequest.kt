package site.jongin.springjwtstudykotlin.dto.request

/*
이런게 data로 선언된 Client는 클래스는 컴파일시에 아래작업이 추가적으로 수행됩니다.

인스턴스간 비교를 위한 equals() 자동생성
Hash 기반 container에서 키로 사용할 수 있도록 hashCode() 자동 생성
property 순서대로 값을 반환해 주는 toString() 자동생성
 */
data class LoginRequest(
    var username: String?,
    var password: String?
) {

    fun validate(): Boolean {
        return !username.isNullOrBlank() && !password.isNullOrBlank()
    }
}
