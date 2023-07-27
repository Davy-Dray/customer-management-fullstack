const  UserProfile = ({name, age, gender ,userId,...props})=>{


    gender = gender === "MALE" ? "men" : "women"

    return (
        <div>
            <p>{name}</p>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${userId}.jpg `} />
            {props.children}
        </div>
    )
}

export default  UserProfile;


