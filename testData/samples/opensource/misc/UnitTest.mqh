/* -*- coding: utf-8 -*-
 *
 * This indicator is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * See a LICENSE file for detail of the license.
 */

#property copyright "Copyright 2014, micclly."
#property link      "https://github.com/micclly"
#property strict

#include <Object.mqh>
#include <Arrays/List.mqh>

class UnitTestData : CObject
{
public:
    string m_name;
    bool m_result;
    string m_message;
    bool m_asserted;

    UnitTestData(string name);
};

UnitTestData::UnitTestData(string name)
: m_name(name), m_result(false), m_message(""), m_asserted(false)
{
}

class UnitTest
{
public:
    UnitTest();
    ~UnitTest();

    void addTest(string name);
    void setSuccess(string name);
    void setFailure(string name, string message);
    void printSummary();

    void assertEquals(string name, string message, bool expected, bool actual);
    void assertEquals(string name, string message, char expected, char actual);
    void assertEquals(string name, string message, uchar expected, uchar actual);
    void assertEquals(string name, string message, short expected, short actual);
    void assertEquals(string name, string message, ushort expected, ushort actual);
    void assertEquals(string name, string message, int expected, int actual);
    void assertEquals(string name, string message, uint expected, uint actual);
    void assertEquals(string name, string message, long expected, long actual);
    void assertEquals(string name, string message, ulong expected, ulong actual);
    void assertEquals(string name, string message, datetime expected, datetime actual);
    void assertEquals(string name, string message, color expected, color actual);
    void assertEquals(string name, string message, float expected, float actual);
    void assertEquals(string name, string message, double expected, double actual);
    void assertEquals(string name, string message, string expected, string actual);


    void assertEquals(string name, string message, const bool& expected[], const bool& actual[]);
    void assertEquals(string name, string message, const char& expected[], const char& actual[]);
    void assertEquals(string name, string message, const uchar& expected[], const uchar& actual[]);
    void assertEquals(string name, string message, const short& expected[], const short& actual[]);
    void assertEquals(string name, string message, const ushort& expected[], const ushort& actual[]);
    void assertEquals(string name, string message, const int& expected[], const int& actual[]);
    void assertEquals(string name, string message, const uint& expected[], const uint& actual[]);
    void assertEquals(string name, string message, const long& expected[], const long& actual[]);
    void assertEquals(string name, string message, const ulong& expected[], const ulong& actual[]);
    void assertEquals(string name, string message, const datetime& expected[], const datetime& actual[]);
    void assertEquals(string name, string message, const color& expected[], const color& actual[]);
    void assertEquals(string name, string message, const float& expected[], const float& actual[]);
    void assertEquals(string name, string message, const double& expected[], const double& actual[]);
    void assertEquals(string name, string message, const string& expected[], const string& actual[]);

    void fail(string name, string message);

private:
    int m_allTestCount;
    int m_successTestCount;
    int m_failureTestCount;
    CList m_testList;

    UnitTestData* findTest(string name);
    void clearTestList();

    bool assertArraySize(string name, string message, const int expectedSize, const int actualSize);
};

UnitTest::UnitTest()
: m_testList(), m_allTestCount(0), m_successTestCount(0)
{
}

UnitTest::~UnitTest(void)
{
    clearTestList();
}

void UnitTest::addTest(string name)
{
    UnitTestData* test = findTest(name);
    if (test != NULL) {
        return;
    }

    m_testList.Add(new UnitTestData(name));
    m_allTestCount += 1;
}

void UnitTest::clearTestList(void)
{
    if (m_testList.GetLastNode() != NULL) {
        while (m_testList.DeleteCurrent())
            ;
    }
}

UnitTestData* UnitTest::findTest(string name)
{
    UnitTestData* data;
    for (data = m_testList.GetFirstNode(); data != NULL; data = m_testList.GetNextNode()) {
        if (data.m_name == name) {
            return data;
        }
    }

    return NULL;
}

void UnitTest::setSuccess(string name)
{
    UnitTestData* test = findTest(name);

    if (test != NULL) {
        if (test.m_asserted) {
            return;
        }

        test.m_result = true;
        test.m_asserted = true;

        m_successTestCount += 1;
        m_failureTestCount = m_allTestCount - m_successTestCount;
    }
}

void UnitTest::setFailure(string name,string message)
{
    UnitTestData* test = findTest(name);

    if (test != NULL) {
        test.m_result = false;
        test.m_message = message;
        test.m_asserted = true;

        m_failureTestCount += 1;
        m_successTestCount = m_allTestCount - m_failureTestCount;
    }
}

void UnitTest::printSummary(void)
{
    UnitTestData* data;

    for (data = m_testList.GetLastNode(); data != NULL; data = m_testList.GetPrevNode()) {
        if (data.m_result) {
            PrintFormat("  %s: OK", data.m_name);
        }
        else {
            PrintFormat("  %s: NG: %s", data.m_name, data.m_message);
        }
    }

    double successPercent = 100.0 * m_successTestCount / m_allTestCount;
    double failurePrcent = 100.0 * m_failureTestCount / m_allTestCount;

    Print("");
    PrintFormat("  Total: %d, Success: %d (%.2f%%), Failure: %d (%.2f%%)",
        m_allTestCount, m_successTestCount, successPercent,
        m_failureTestCount, failurePrcent);
    Print("UnitTest summary:");
}

void UnitTest::assertEquals(string name, string message, bool expected, bool actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        string m;
        StringConcatenate(m, message, ": expected is <", expected, "> but <", actual, ">");
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, char expected, char actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + CharToString(expected) +
            "> but <" + CharToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, uchar expected, uchar actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + CharToString(expected) +
            "> but <" + CharToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, short expected, short actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, ushort expected, ushort actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, int expected, int actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, uint expected, uint actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, long expected, long actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, ulong expected, ulong actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + IntegerToString(expected) +
            "> but <" + IntegerToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, datetime expected, datetime actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + TimeToString(expected) +
            "> but <" + TimeToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, color expected, color actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + ColorToString(expected) +
            "> but <" + ColorToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, float expected, float actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + DoubleToString(expected) +
            "> but <" + DoubleToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, double expected, double actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + DoubleToString(expected) +
            "> but <" + DoubleToString(actual) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

void UnitTest::assertEquals(string name, string message, string expected, string actual)
{
    if (expected == actual) {
        setSuccess(name);
    }
    else {
        const string m = message + ": expected is <" + expected +
            "> but <" + actual + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
    }
}

bool UnitTest::assertArraySize(string name, string message, const int expectedSize, const int actualSize)
{
    if (expectedSize == actualSize) {
        return true;
    }
    else {
        const string m = message + ": expected array size is <" + IntegerToString(expectedSize) +
            "> but <" + IntegerToString(actualSize) + ">";
        setFailure(name, m);
        Alert("Test failed: " + name + ": " + m);
        return false;
    }

}

void UnitTest::assertEquals(string name, string message, const bool& expected[], const bool& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            string m;
            StringConcatenate(m, message, ": expected array[", IntegerToString(i), "] is <",
                expected[i], "> but <", actual[i], ">");
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const char& expected[], const char& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                CharToString(expected[i]) +
                "> but <" + CharToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const uchar& expected[], const uchar& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                CharToString(expected[i]) +
                "> but <" + CharToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const short& expected[], const short& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const ushort& expected[], const ushort& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const int& expected[], const int& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const uint& expected[], const uint& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const long& expected[], const long& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const ulong& expected[], const ulong& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                IntegerToString(expected[i]) +
                "> but <" + IntegerToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const datetime& expected[], const datetime& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                TimeToString(expected[i]) +
                "> but <" + TimeToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const color& expected[], const color& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                ColorToString(expected[i]) +
                "> but <" + ColorToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::assertEquals(string name, string message, const float& expected[], const float& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                DoubleToString(expected[i]) +
                "> but <" + DoubleToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}


void UnitTest::assertEquals(string name, string message, const double& expected[], const double& actual[])
{
    const int expectedSize = ArraySize(expected);
    const int actualSize = ArraySize(actual);

    if (!assertArraySize(name, message, expectedSize, actualSize)) {
        return;
    }

    for (int i = 0; i < actualSize; i++) {
        if (expected[i] != actual[i]) {
            const string m = message + ": expected array[" + IntegerToString(i) + "] is <" +
                DoubleToString(expected[i]) +
                "> but <" + DoubleToString(actual[i]) + ">";
            setFailure(name, m);
            Alert("Test failed: " + name + ": " + m);
            return;
        }
    }

    setSuccess(name);
}

void UnitTest::fail(string name, string message)
{
    setFailure(name, message);
    Alert("Test faied: " + name + ": " + message);
}