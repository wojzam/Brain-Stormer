import "./../css/header.css";

export default function Header() {
  return (
    <>
      <header>
        <div className="navbar">
          <div className="page-title-container">
            <a className="page-title" href="/">
              Brain Stormer
            </a>
          </div>
          <a href="explore"> Explore </a>
          <a href="create"> Create </a>
          <a href="topic"> Topic </a>
        </div>
        <div id="login-signup">
          <a className="login-btn" href="login">
            Login
          </a>
          <a className="signup-btn" href="signup">
            Sign up
          </a>
        </div>
      </header>
    </>
  );
}
