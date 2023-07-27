import {
  Button,
  Drawer,
  DrawerBody,
  DrawerCloseButton,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  useDisclosure,
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";
import { EditIcon } from "@chakra-ui/icons";
import React from "react";

const UpdateCustomerDrawer = ({
  fetchCustomers,
  id
}) => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <EditIcon
        boxSize={3}
        marginRight={4}
        _hover={{
          transform: "scale(1.1)",
          boxShadow: "lg",
        }}
        onClick={onOpen}
      />

      <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>Create new customer</DrawerHeader>

          <DrawerBody>
            <UpdateCustomerForm
              fetchCustomers={fetchCustomers}
              name={name}
              email={email}
              age={age}
              gender={gender}
              id={id}
            />
          </DrawerBody>

          <DrawerFooter>
            <Button onClick={onClose} backgroundColor={"teal"}>
              Close
            </Button>
          </DrawerFooter>
        </DrawerContent>
      </Drawer>
    </>
  );
};

export default UpdateCustomerDrawer;
