package font;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;



/**
 *      This is an abstract class that provides the functionality
 *      of WordWrap.  The extended classes should provide the methods
 *      to process the fundamental operations accustomed with a 
 *      font.<br>
 *      Rendering of the fonts is a method of the font itself,
 *      no longer the Graphics Library.
 */
public abstract class IFont
{
	public static final int ALIGH_LEFT = 1;
	public static final int ALIGH_RIGHT = 2;
	public static final int ALIGH_CENTER = 3;
	
    /**
     * Return the width in pixels of a string
     * @param aText The input string
     * @return The width in pixels of a string
     */
	public abstract int getStringWidth(String aText);
    
    /**
     * Return the height of the font in pixels
     * @return The height of the font in pixels
     */
	public abstract int getRowHeight();
    /**
     * Draw a string to the specified Canvas library
     * @param canvas The Canvas library
     * @param aString A input string
     * @param aX The X-axis position to which the string will be drawn
     * @param aY The Y-axis position to which the string will be drawn
     */
    public abstract void drawString( Canvas canvas, String aString, int aX, int aY);
    /**
     * Draw a string to the specified Canvas library with an anchor
     * @param canvas The Canvas library
     * @param aString A input string
     * @param aX The X-axis position to which the string will be drawn
     * @param aY The Y-axis position to which the string will be drawn
     * @param aAnchor The anchor
     */
    public abstract void drawString( Canvas canvas, String aString, int aX, int aY, int aAnchor);

    /**
     * Checks if a character if a carriage return can be replaced with this character
     * @param aChar the character to check for whitespace equivalency
     * @return returns true if the character is a space or carriage return.
     */
    private boolean isWhitespace( char aChar )
    {
        return(aChar == ' ' || aChar == '\n');
    }

    /**
     * Breaks a <code>String</code> into a <code>Vector</code> of smaller <code>String</code>s based on a width
     * @param   aString         the <code>String</code> to parse
     * @param   aWindowWidth    the width of the drawing area in pixels
     * @return  a <code>Vector</code> of smaller <code>String</code>s where each <code>String</code>'s display
     * is at most a width of <code>aWindowWidth</code>
     */
    public List<String> generateWordWrap( String aString, int aWindowWidth )
    {
    	List<String> vectorLines  = new ArrayList<String>();
        //vectorLines.removeAllElements();
        int curPosition = 0;
        while ( curPosition < aString.length() )
        {
            int startPosition   = curPosition;
            int endPosition     = curPosition;

            // Add the first word.
            while ( (curPosition < aString.length()) && !isWhitespace(aString.charAt(curPosition)) )
            {
                curPosition++;
            }

            // See if the first word is too long.
            int substringWidth = getStringWidth(aString.substring(startPosition, curPosition));
            if ( substringWidth > aWindowWidth )
            {
                do
                {
                    curPosition--;
                    substringWidth -= getStringWidth(String.valueOf(aString.charAt(curPosition)));
                }
                while ( substringWidth > aWindowWidth );
                // Set the end of the line.
                endPosition = curPosition;
            }
            else
            {
                boolean isLineDone = false;
                do
                {
                    int endOfWordPosition = curPosition;
                    // We just added the first word.  Add the next block of whitespace.
                    // The character at curPosition is whitespace.
                    while ( curPosition < aString.length() && aString.charAt(curPosition) == ' ' )
                    {
                        curPosition++;
                    }

                    // Check to see how we broke out of the while loop.
                    if ( curPosition == aString.length() )
                    {
                        endPosition = endOfWordPosition;
                        isLineDone = true;
                    }
                    else
                    {
                        switch ( aString.charAt(curPosition) )
                        {
                            case '\n':
                                endPosition = endOfWordPosition;
                                curPosition++;
                                isLineDone = true;
                                break;
                            default:
                                if ( getStringWidth(aString.substring(startPosition, curPosition)) > aWindowWidth )
                                {
                                    endPosition = endOfWordPosition;
                                    isLineDone = true;
                                }
                                break;
                        }
                    }

                    if ( !isLineDone )
                    {
                        endOfWordPosition = curPosition;
                        // Add the next block of characters.
                        do
                        {
                            curPosition++;
                        }
                        while ( (curPosition < aString.length()) && !isWhitespace(aString.charAt(curPosition)) );

                        // See if we overflow.
                        if ( getStringWidth(aString.substring(startPosition, curPosition)) > aWindowWidth )
                        {
                            endPosition = endOfWordPosition;
                            curPosition = endOfWordPosition;
                            isLineDone = true;
                        }
                    }
                }
                while ( !isLineDone );
            }
            // Add the line to the list.
            vectorLines.add(aString.substring(startPosition, endPosition));
        }
        return vectorLines;
    }
    
    /**
     * Draws the specified word wrap to a specified Canvas library and location
     * @param   canvas             the Canvas library to which the word wrap will be drawn
     * @param   aLines          contains a <code>Vector</code> of <code>String</code>s with each line of the word wrap
     * @param   aX              the X-axis position to which the word wrap will be drawn
     * @param   aY              the Y-axis position to which the word wrap will be drawn
     * @param   aLineSpacing    the size of spacing in between lines in pixels
     * @return                  the height of the word wrap
     * @see     getWordWrapHeight(Vector, int)
     */
                                                          
    public int drawWordWrap(Canvas canvas, List<String> aLines, int aX, int aY, int aLineSpacing )
    { 	
        int    mY       = aY;
        int    mX       = aX;
        String mString  = "";
        for ( int i = 0; i < aLines.size(); i++ )
        {
            mString = aLines.get(i);
            drawString(canvas, mString, mX, mY);
            mY += aLineSpacing;
        }
        return(aLines.size() * getRowHeight()) + ((aLines.size() - 1) * (aLineSpacing - getRowHeight()));
    }
    
    /**
     * Draws the specified word wrap to a specified Canvas library and location
     * @param   canvas             the Canvas library to which the word wrap will be drawn
     * @param   aLines          contains a <code>Vector</code> of <code>String</code>s with each line of the word wrap
     * @param   aX              the X-axis position to which the word wrap will be drawn
     * @param   aY              the Y-axis position to which the word wrap will be drawn
     * @param   aLineSpacing    the size of spacing in between lines in pixels
     * @return                  the height of the word wrap
     * @see     getWordWrapHeight(Vector, int)
     */

    public int drawWordWrap( Canvas canvas, List<String> aLines, int aX, int aY, int aLineSpacing, int anchor )
    {
        int    mY       = aY;
        int    mX       = aX;
        String mString  = "";
        for ( int i = 0; i < aLines.size(); i++ )
        {
            mString = aLines.get(i);
            drawString(canvas, mString, mX, mY,anchor);
            mY += aLineSpacing;
        }
        return(aLines.size() * getRowHeight()) + ((aLines.size() - 1) * (aLineSpacing - getRowHeight()));
    }
}
  
