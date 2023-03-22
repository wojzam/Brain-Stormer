import BackButton from "../components/BackButton";
import Idea from "../components/Idea";
import Button from "@mui/material/Button";

const Topic = () => {
  return (
    <>
      <BackButton />
      <h1 className="page-title">Topic</h1>
      <Idea text="Test Idea 1" />
      <Idea text="Test Idea 2" />
      <Idea text="Test Idea 3" />
      <Button fullWidth variant="text" sx={{ mt: 3, mb: 2 }}>
        Add Idea
      </Button>
    </>
  );
};

export default Topic;
