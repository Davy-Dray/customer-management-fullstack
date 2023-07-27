import {
    Box,
    chakra,
    Flex,
    SimpleGrid,
    Stat,
    StatLabel,
    StatNumber,
    useColorModeValue,
} from '@chakra-ui/react';
import { BsPerson } from 'react-icons/bs';
import CreateCustomerDrawer from "./CreateCustomerDrawer.jsx";

function StatsCard({title,stat,icon}) {

    return (
        <Stat
            px={{ base: 2, md: 4 }}
            py={'5'}
            shadow={'xl'}
            border={'1px solid'}
            borderColor={useColorModeValue('gray.800', 'gray.500')}
            rounded={'lg'}>
            <Flex justifyContent={'space-between'}>
                <Box pl={{ base: 2, md: 4 }}>
                    <StatLabel fontWeight={'medium'} isTruncated>
                        {title}
                    </StatLabel>


                    <StatNumber fontSize={'2xl'} fontWeight={'medium'}>
                        {stat}
                    </StatNumber>
                </Box>
                <Box
                    my={'auto'}
                    color={useColorModeValue('gray.800', 'gray.200')}
                    alignContent={'center'}>
                    {icon}
                </Box>
            </Flex>
        </Stat>
    );
}

export default function BasicStatistics({noOfCustomers, fetchCustomers}) {
    return (
        <Box maxW="7xl" mx={'auto'} pt={5} px={{ base: 2, sm: 12, md: 17 }} mb={10}>
            <chakra.h1
                textAlign={'center'}
                fontSize={'4xl'}
                py={10}
                fontWeight={'bold'}>
               World Best Products.
            </chakra.h1>
            <SimpleGrid columns={{ base: 1, md: 3 }} spacing={{ base: 5, lg: 8 }}  mb={'55px'}>
                <StatsCard
                    mb={'3px'}
                    title={'Customers'}
                    stat={noOfCustomers}
                    icon={<BsPerson size={'3em'} />}
                />

            </SimpleGrid>

            <CreateCustomerDrawer
                fetchCustomers ={fetchCustomers}
            ></CreateCustomerDrawer>

        </Box>
    );
}