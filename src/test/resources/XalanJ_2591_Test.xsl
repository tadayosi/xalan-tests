<?xml version="1.0" ?>
<xsl:stylesheet exclude-result-prefixes="xsl" version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <output>
      <message type="HELLO">
        Hello, <xsl:value-of select="input/name" />!
      </message>
    </output>
  </xsl:template>
</xsl:stylesheet>