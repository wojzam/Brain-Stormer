import lightBulb from './../assets/light-bulb.svg'

export default function Welcome() {
    return (
        <>
            <div className="wrapper">
                <h1>Welcome to Brain Stormer</h1>
                <h2>unleash your creativity and collaborate to bring ideas to life</h2>
                <img src={lightBulb} alt="Light bulb" width={200} height={200}/>
            </div>
        </>
    )
}