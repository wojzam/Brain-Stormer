import lightBulb from "./../assets/light-bulb.svg";
import "./../css/home.css";

const Home = () => {
  return (
    <>
      <div className="welcome-text">
        <h1>Welcome to Brain Stormer</h1>
        <h2>unleash your creativity and collaborate to bring ideas to life</h2>
        <button className="big-btn">START HERE</button>
      </div>
      <img src={lightBulb} alt="Light bulb" />
    </>
  );
};

export default Home;
